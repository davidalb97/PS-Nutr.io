package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.RestaurantResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.model.Restaurant
import java.util.concurrent.CompletableFuture

private const val MAX_RADIUS = 1000


@Service
class RestaurantService(
        private val dbRestaurantRepository: RestaurantDbRepository,
        private val restaurantApiMapper: RestaurantApiMapper,
        private val restaurantResponseMapper: RestaurantResponseMapper
) {

    fun getNearbyRestaurants(
            latitude: Float,
            longitude: Float,
            name: String?,
            radius: Int?,
            apiType: String?
    ): Set<Restaurant> {
        val chosenRadius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS
        val type = RestaurantApiType.getOrDefault(apiType)
        val restaurantApi = restaurantApiMapper.getRestaurantApi(type)

        val apiRestaurants = CompletableFuture
                .supplyAsync { restaurantApi.searchNearbyRestaurants(latitude, longitude, chosenRadius, name) }
                .thenApply { it.map(restaurantResponseMapper::mapTo) }

        val dbRestaurants = dbRestaurantRepository
                .getAllByCoordinates(latitude, longitude, chosenRadius)
                .map(restaurantResponseMapper::mapTo)

        //TODO Handle CompletableFuture exception
        return filterRedundantApiRestaurants(dbRestaurants, apiRestaurants.get())
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
    fun getRestaurant(id: String, apiType: String?): Restaurant? {
        val type = RestaurantApiType.getOrDefault(apiType)
        val restaurantApi = restaurantApiMapper.getRestaurantApi(type)

        //If 'id' cannot be converted to Integer, then don't search in database.
        //This is a specification that happens because all restaurants are submissions, and as such,
        //their unique identifier is always an auto-incremented integer
        val restaurant = id
                .toIntOrNull()
                ?.let { dbRestaurantRepository.getById(it) }
                ?: restaurantApi.getRestaurantInfo(id)

        return restaurant?.let(restaurantResponseMapper::mapTo)
    }

    private fun filterRedundantApiRestaurants(dbRestaurants: Collection<Restaurant>, apiRestaurants: Collection<Restaurant>): Set<Restaurant> {
        val result = dbRestaurants.toMutableSet()

        apiRestaurants.forEach {
            if (!dbContainsRestaurant(result, it)) {
                result.add(it)
            }
        }
        return result
    }

    private fun dbContainsRestaurant(dbRestaurants: Collection<Restaurant>, restaurant: Restaurant): Boolean {
        return dbRestaurants.any { dbRestaurant -> restaurant.identifier == dbRestaurant.identifier }
    }
}