package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbRestaurantRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import pt.isel.ps.g06.httpserver.model.Restaurant
import java.util.concurrent.CompletableFuture

private const val MAX_RADIUS = 1000


@Service
class RestaurantService(
        private val dbRestaurantRepository: DbRestaurantRepository,
        private val restaurantApiMapper: RestaurantApiMapper
) {

    fun getNearbyRestaurants(latitude: Float?, longitude: Float?, radius: Int? = MAX_RADIUS, apiType: String?): Set<Restaurant> {
        return if (latitude == null || longitude == null) emptySet() else {
            val chosenRadius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS
            val type = RestaurantApiType.getOrDefault(apiType)

            val restaurantApi = restaurantApiMapper.getRestaurantApi(type)

            val apiRestaurants = CompletableFuture
                    .supplyAsync { restaurantApi.searchRestaurants(latitude, longitude, chosenRadius) }
                    .thenApply { it.map(this::mapToRestaurant) }

            val dbRestaurants = dbRestaurantRepository
                    .getRestaurantsByCoordinates(latitude, longitude, chosenRadius)
                    .map(this::mapToRestaurant)

            //TODO Handle CompletableFuture exception
            return filterRedundantApiRestaurants(dbRestaurants, apiRestaurants.get())
        }
    }

    /**
     * Obtain more information for a Restaurant with given id.
     *
     * Current search algorithm will first query the Database for any restaurant and if none was found,
     * search the preferred Restaurant API (Zomato, etc.)
     *
     * @param apiType - describes which api to search the Restaurant. See [RestaurantApiType] for possible types.
     * Defaults to [RestaurantApiType.Zomato]
     */
    fun getRestaurant(id: Int, apiType: String?): Restaurant? {
        val type = RestaurantApiType.getOrDefault(apiType)
        val restaurantApi = restaurantApiMapper.getRestaurantApi(type)
        val restaurant = dbRestaurantRepository.getRestaurantById(id) ?: restaurantApi.getRestaurantInfo(id)

        return restaurant?.let(this::mapToRestaurant)
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
        return dbRestaurants.any { dbRestaurant -> restaurant.apiId == dbRestaurant.apiId }
    }

    private fun mapToRestaurant(dto: RestaurantDto): Restaurant = Restaurant(
            dto.id,
            dto.name,
            dto.latitude,
            dto.longitude,
            dto.cuisines
    )
}