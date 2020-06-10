package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.RestaurantResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantApiId
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Restaurant

private const val MAX_RADIUS = 1000


@Service
class RestaurantService(
        private val dbRestaurantRepository: RestaurantDbRepository,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val restaurantApiMapper: RestaurantApiMapper,
        private val restaurantResponseMapper: RestaurantResponseMapper
        /*,
        private val transactionHolder: TransactionHolder*/
) {

    fun getNearbyRestaurants(
            latitude: Float,
            longitude: Float,
            name: String?,
            radius: Int?,
            apiType: String?
    ): Collection<Restaurant> {
        val chosenRadius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS
        val type = RestaurantApiType.getOrDefault(apiType)
        val restaurantApi = restaurantApiMapper.getRestaurantApi(type)

        //Get API restaurants
        val apiRestaurants =
                restaurantApi.searchNearbyRestaurants(latitude, longitude, chosenRadius, name)
                        .thenApply {
                            it.map(restaurantResponseMapper::mapTo)
                        }

        return dbRestaurantRepository
                .getAllByCoordinates(latitude, longitude, chosenRadius)
                .map(restaurantResponseMapper::mapTo)
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
    fun getRestaurant(id: String, apiType: String? = null): Restaurant? {
        val type = RestaurantApiType.getOrDefault(apiType)
        val restaurantApi = restaurantApiMapper.getRestaurantApi(type)

        //If 'id' cannot be converted to Integer, then don't search in database.
        //This is a specification that happens because all restaurants are submissions, and as such,
        //their unique identifier is always an auto-incremented integer
        val restaurant = id
                .toIntOrNull()
                ?.let { dbRestaurantRepository.getById(it) }
                ?: restaurantApi.getRestaurantInfo(id).get()

        return restaurant?.let(restaurantResponseMapper::mapTo)
    }

    //Map<id, source>
    //submitter=API, id=10
    //if(is in map)-> API, check API Restaurant
    //else -> check user submissionID

    fun createRestaurant(
            submitterId: Int,
            restaurantName: String,
            apiId: RestaurantApiId? = null,
            cuisines: Collection<String>,
            latitude: Float,
            longitude: Float
    ): Restaurant {
        val createdRestaurant = dbRestaurantRepository.insert(
                submitterId = submitterId,
                restaurantName = restaurantName,
                apiId = apiId,
                cuisineNames = cuisines,
                latitude = latitude,
                longitude = longitude
        )

        return restaurantResponseMapper.mapTo(createdRestaurant)
    }

    fun addRestaurantMeal(submitterId: Int, meal: Meal, apiSubmitter: Int, restaurantApiId: String?, submissionId: Int?) {
        val restaurantSubmissionId: Int = submissionId ?: getRestaurant(restaurantApiId!!)?.let { restaurant ->
            createRestaurant(
                    submitterId = submitterId,
                    restaurantName = restaurant.name,
                    apiId = RestaurantApiId(restaurantApiId, RestaurantApiType.Here),   //TODO Restaurant Type
                    cuisines = restaurant.cuisines.value,
                    latitude = restaurant.latitude,
                    longitude = restaurant.longitude
            )
        }?.identifier?.toInt()  //TODO What an anti-pattern here
        //TODO Better exception
        ?: throw NoSuchElementException()

        dbRestaurantMealRepository.insert(
                submitterId = submitterId,
                mealId = meal.identifier,
                restaurantId = restaurantSubmissionId
        )
    }

    private fun filterRedundantApiRestaurants(dbRestaurants: Collection<Restaurant>, apiRestaurants: Collection<Restaurant>): Collection<Restaurant> {
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