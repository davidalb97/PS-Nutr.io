package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.RestaurantInfoResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.RestaurantItemResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.RestaurantInput
import pt.isel.ps.g06.httpserver.model.RestaurantInfo
import pt.isel.ps.g06.httpserver.model.RestaurantItem

private const val MAX_RADIUS = 1000


@Service
class RestaurantService(
        private val dbRestaurantRepository: RestaurantDbRepository,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val dbMealRepository: MealDbRepository,
        private val restaurantApiMapper: RestaurantApiMapper,
        private val restaurantInfoResponseMapper: RestaurantInfoResponseMapper,
        private val restaurantItemResponseMapper: RestaurantItemResponseMapper
        /*,
        private val transactionHolder: TransactionHolder*/
) {

    fun getNearbyRestaurants(
            latitude: Float,
            longitude: Float,
            name: String?,
            radius: Int?,
            apiType: String?,
            userId: Int?
    ): Collection<RestaurantItem> {
        val chosenRadius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS
        val type = RestaurantApiType.getOrDefault(apiType)
        val restaurantApi = restaurantApiMapper.getRestaurantApi(type)

        //Get API restaurants
        val apiRestaurants =
                restaurantApi.searchNearbyRestaurants(latitude, longitude, chosenRadius, name)
                        .thenApply {
                            it.map{restaurantItemResponseMapper.mapTo(it) }
                        }

        return dbRestaurantRepository
                .getAllByCoordinates(latitude, longitude, chosenRadius, userId)
                .map{restaurantItemResponseMapper.mapTo(it) }
                .let { filterRedundantApiRestaurants(it, apiRestaurants.get()) }

        //Keeps an open transaction while we iterate the DB response Stream
//        return transactionHolder.inTransaction {
//
//            //Get db restaurants
//            dbRestaurantRepository
//                    .getAllByCoordinates(latitude, longitude, chosenRadius)
//                    //Must close database resources even when database row iteration fails
//                    .use {
//                        //Convert to sequence & map
//                        val mapped = it.asSequence()
//                                .map(restaurantResponseMapper::mapTo)
//
//                        //Return all restaurants
//                        return@inTransaction filterRedundantApiRestaurants(mapped, apiRestaurants.get())
//                    }
//        }
    }

    /**
     * Obtain more information for a Restaurant with given id.
     *
     * Current search algorithm will first query the Database for any restaurant and if none was found,
     * search the preferred Restaurant API (Zomato, Here, etc.)
     *
     * @param apiType describes which api to search the Restaurant. See [RestaurantApiType] for possible types.
     * Defaults to [RestaurantApiType.Here]
     */
    fun getRestaurant(id: String, apiType: String?, userId: Int?): RestaurantInfo? {
        val type = RestaurantApiType.getOrDefault(apiType)
        val restaurantApi = restaurantApiMapper.getRestaurantApi(type)

        //If 'id' cannot be converted to Integer, then don't search in database.
        //This is a specification that happens because all restaurants are submissions, and as such,
        //their unique identifier is always an auto-incremented integer

        val restaurant = id
                .toIntOrNull()
                ?.let {
                    val restaurantMeals = dbRestaurantMealRepository.getItemsByRestaurantId(it, userId)
                    val restaurantCuisines = dbRestaurantRepository.getRestaurantCuisines(it).map {
                        it.cuisine_name
                    }
                    val suggestedMeals = dbMealRepository.getAllByCuisineNames(restaurantCuisines, userId)
                    dbRestaurantRepository.getInfoById(it, userId, suggestedMeals, restaurantMeals)
                }
                ?: restaurantApi.getRestaurantInfo(id).get()

        return restaurant?.let {
            restaurantInfoResponseMapper.mapTo(it, userId)
        }
    }

    fun createRestaurant(restaurant: RestaurantInput): DbSubmissionDto {
        return dbRestaurantRepository.insert(
                submitterId = restaurant.submitterId!!,
                restaurantName = restaurant.name!!,
                apiId = null,
                cuisineNames = restaurant.cuisines!!,
                latitude = restaurant.latitude!!,
                longitude = restaurant.longitude!!
        )
    }

    private fun filterRedundantApiRestaurants(dbRestaurants: Collection<RestaurantItem>, apiRestaurants: Collection<RestaurantItem>): Collection<RestaurantItem> {
        //Join db restaurants with filtered api restaurants
        return dbRestaurants.union(
                //Filter api restaurants that already exist in db
                apiRestaurants.filter { apiRestaurant ->
                    //Db does not contain a restaurant with the api identifier
                    dbRestaurants.none { dbRestaurant -> apiRestaurant.identifier == dbRestaurant.identifier }
                }
        )

//        val result = dbRestaurants.toMutableSet()
//
//        apiRestaurants.forEach {
//            if (!dbContainsRestaurant(result, it)) {
//                result.add(it)
//            }
//        }
//        return result
    }

//    private fun dbContainsRestaurant(dbRestaurants: Collection<Restaurant>, restaurant: Restaurant): Boolean {
//        return dbRestaurants.any { dbRestaurant -> restaurant.identifier == dbRestaurant.identifier }
//    }
}