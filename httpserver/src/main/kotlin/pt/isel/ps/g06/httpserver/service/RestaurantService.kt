package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.NoSuchApiResponseStatusException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.RestaurantResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import pt.isel.ps.g06.httpserver.model.Restaurant

private const val MAX_RADIUS = 1000

@Service
class RestaurantService(
        private val dbRestaurantRepository: RestaurantDbRepository,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val restaurantApiMapper: RestaurantApiMapper,
        private val restaurantResponseMapper: RestaurantResponseMapper,
        private val apiSubmitterMapper: ApiSubmitterMapper
) {

    fun getNearbyRestaurants(
            latitude: Float,
            longitude: Float,
            name: String?,
            radius: Int?,
            apiType: String?
    ): Sequence<Restaurant> {
        val chosenRadius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS
        val type = RestaurantApiType.getOrDefault(apiType)
        val restaurantApi = restaurantApiMapper.getRestaurantApi(type)

        //Get API restaurants
        val apiRestaurants =
                restaurantApi.searchNearbyRestaurants(latitude, longitude, chosenRadius, name)
                        .thenApply {
                            it.map(restaurantResponseMapper::mapTo)
                        }

        return dbRestaurantRepository.getAllByCoordinates(latitude, longitude, chosenRadius)
                .map(restaurantResponseMapper::mapTo)
                .let { filterRedundantApiRestaurants(it, apiRestaurants.get().asSequence()) }

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
     * Search algorithm will first query the Database for any restaurant and if none was found,
     * search the preferred Restaurant API (Zomato, Here, etc.)
     */
    fun getRestaurant(submitterId: Int, submissionId: Int?, apiId: String?): Restaurant? {
        val restaurant = when {
            submissionId != null -> searchRestaurantSubmission(submissionId)

            apiId != null -> {
                val apiType = apiSubmitterMapper
                        .getApiType(submitterId)
                        ?: throw NoSuchApiResponseStatusException()

                searchApiRestaurant(submitterId, apiId, apiType)
            }

            else -> null
        }

        return restaurant?.let { restaurantResponseMapper.mapTo(it) }
    }

    /**
     * Creates a restaurant on the Database, where the submitter can either be an API or a user.
     * @param apiId not null determines if submitter is an API; else User.
     *
     * @return the Submission identifier of created Restaurant
     */
    fun createRestaurant(
            submitterId: Int,
            apiId: String? = null,
            restaurantName: String,
            cuisines: Collection<String>,
            latitude: Float,
            longitude: Float
    ): Restaurant {
        if (apiId != null) {
            //Verify is given submitterId belongs to an API
            apiSubmitterMapper.getApiType(submitterId) ?: throw NoSuchApiResponseStatusException()
        }

        val createdRestaurant = dbRestaurantRepository.insert(
                submitterId = submitterId,
                apiId = apiId,
                restaurantName = restaurantName,
                cuisineNames = cuisines,
                latitude = latitude,
                longitude = longitude
        )

        return restaurantResponseMapper.mapTo(createdRestaurant)
    }


    private fun filterRedundantApiRestaurants(dbRestaurants: Sequence<Restaurant>, apiRestaurants: Sequence<Restaurant>): Sequence<Restaurant> {
        //Join db restaurants with filtered api restaurants
        return dbRestaurants.plus(
                //Filter api restaurants that already exist in db
                apiRestaurants.filter { apiRestaurant ->
                    //Db does not contain a restaurant with the api identifier
                    dbRestaurants.none { dbRestaurant -> apiRestaurant.identifier == dbRestaurant.identifier }
                }
        )
    }

    private fun searchApiRestaurant(apiSubmitterId: Int, apiId: String, apiType: RestaurantApiType): RestaurantDto? {
        val restaurantApi = restaurantApiMapper.getRestaurantApi(apiType)

        //TODO Exception handle on CompletableFuture
        return dbRestaurantRepository
                .getApiRestaurant(apiSubmitterId, apiId)
                ?: restaurantApi.getRestaurantInfo(apiId).get()
    }

    private fun searchRestaurantSubmission(submissionId: Int): RestaurantDto? {
        return dbRestaurantRepository.getById(submissionId)
    }
}