package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.HereRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.mapper.restaurant.DbRestaurantResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.model.mapper.restaurant.RestaurantResponseMapper
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.NoSuchApiResponseStatusException
import pt.isel.ps.g06.httpserver.exception.problemJson.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.util.log

private const val MAX_RADIUS = 1000

@Service
class RestaurantService(
        private val dbRestaurantRepository: RestaurantDbRepository,
        private val hereRestaurantApi: HereRestaurantApi,
        private val restaurantResponseMapper: RestaurantResponseMapper,
        private val dbRestaurantResponseMapper: DbRestaurantResponseMapper,
        private val apiSubmitterMapper: ApiSubmitterMapper,
        private val dbFavoriteDbRepository: FavoriteDbRepository,
        private val dbReportDbRepository: ReportDbRepository
) {

    fun setFavorite(restaurantId: Int, userId: Int, isFavorite: Boolean): Boolean {
        return dbFavoriteDbRepository.setFavorite(restaurantId, userId, isFavorite)
    }

    fun getNearbyRestaurants(
            latitude: Float,
            longitude: Float,
            name: String?,
            radius: Int?,
            skip: Int?,
            count: Int
    ): Sequence<Restaurant> {
        val chosenRadius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS

        //Get API restaurants
        val apiRestaurants = hereRestaurantApi
                .searchNearbyRestaurants(latitude, longitude, chosenRadius, name, skip, count / 2)
                .thenApply { it.map(restaurantResponseMapper::mapTo) }

        val databaseRestaurants = dbRestaurantRepository
                .getAllByCoordinates(latitude, longitude, chosenRadius, skip, count / 2)
                .map(restaurantResponseMapper::mapTo)

        //TODO Make Get call from API return a Stream
        return filterRedundantApiRestaurants(databaseRestaurants, apiRestaurants.get().asSequence())
    }

    /**
     * Obtain more information for a Restaurant with given id.
     *
     * Search algorithm will first query the Database for any restaurant and if none was found,
     * search the preferred Restaurant API (See [RestaurantApiType] for supported APIs)
     */
    fun getRestaurant(submitterId: Int, submissionId: Int?, apiId: String?): Restaurant? {
        val restaurant = when {
            //If submissionId exists, get information from the database
            submissionId != null -> dbRestaurantRepository.getById(submissionId)
            apiId != null -> searchApiRestaurant(submitterId, apiId)
            else -> null
        }

        return restaurant?.let { restaurantResponseMapper.mapTo(it) }
    }

    fun getRestaurant(restaurantIdentifier: RestaurantIdentifier): Restaurant? {
        return getRestaurant(
                submitterId = restaurantIdentifier.submitterId,
                submissionId = restaurantIdentifier.submissionId,
                apiId = restaurantIdentifier.apiId
        )
    }

    fun getOrInsertRestaurant(restaurantIdentifier: RestaurantIdentifier): Restaurant {
        var restaurant = getRestaurant(restaurantIdentifier) ?: throw RestaurantNotFoundException()
        restaurant = createRestaurantIfAbsent(restaurant)
        return restaurant
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
            longitude: Float,
            ownerId: Int?
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
                longitude = longitude,
                ownerId = ownerId
        )

        return restaurantResponseMapper.mapTo(createdRestaurant)
    }

    /**
     * Creates a new database restaurant if it does not exist
     */
    fun createRestaurantIfAbsent(restaurant: Restaurant): Restaurant {

        if (!restaurant.identifier.value.isPresentInDatabase()) {
            return createRestaurant(
                    submitterId = restaurant.identifier.value.submitterId,
                    apiId = restaurant.identifier.value.apiId,
                    restaurantName = restaurant.name,
                    //TODO use cuisine mapper
                    //TODO Avoid eager call
                    cuisines = restaurant.cuisines.map { it.name }.toList(),
                    latitude = restaurant.latitude,
                    longitude = restaurant.longitude,
                    ownerId = restaurant.ownerId
            )
        }
        return restaurant
    }

    fun addReport(submitterId: Int, restaurantIdentifier: RestaurantIdentifier, report: String) {
        val restaurant = getOrInsertRestaurant(restaurantIdentifier)

        dbReportDbRepository.insert(submitterId, restaurant.identifier.value.submissionId!!, report)
    }

    fun addOwner(restaurantId: Int, ownerId: Int) {
        dbRestaurantRepository.addOwner(restaurantId, ownerId)
    }

    private fun filterRedundantApiRestaurants(
            dbRestaurants: Sequence<Restaurant>,
            apiRestaurants: Sequence<Restaurant>
    ): Sequence<Restaurant> {

        //Filter api restaurants that already exist in db
        val filteredApiRestaurants = apiRestaurants.filter { apiRestaurant ->
            //Db does not contain a restaurant with the api identifier
            dbRestaurants.none { dbRestaurant ->
                //Same apiId
                apiRestaurant.identifier.value.apiId == dbRestaurant.identifier.value.apiId
                        //Same API
                        && apiRestaurant.identifier.value.submitterId == dbRestaurant.identifier.value.submitterId
            }
        }

        return dbRestaurants.plus(filteredApiRestaurants)
    }

    /**
     * Prioritize database results first, as requester might have an outdated Restaurant Identifier.
     */
    private fun searchApiRestaurant(apiSubmitterId: Int, apiId: String): RestaurantDto? {
        return dbRestaurantRepository.getApiRestaurant(apiSubmitterId, apiId) ?: hereRestaurantApi
                .getRestaurantInfo(apiId)
                .exceptionally {
                    log("Get Restaurant from API produced following exception: ${it.message}")
                    null
                }
                .get()
    }

    fun getRestaurantSubmission(submissionId: Int): Restaurant? {
        return dbRestaurantRepository
                .getById(submissionId)
                ?.let(dbRestaurantResponseMapper::mapTo)
    }

    fun getUserFavoriteRestaurants(submitterId: Int, count: Int?, skip: Int?): Sequence<Restaurant> =
            dbRestaurantRepository
                    .getAllUserFavorites(submitterId, count, skip)
                    .map(restaurantResponseMapper::mapTo)
}