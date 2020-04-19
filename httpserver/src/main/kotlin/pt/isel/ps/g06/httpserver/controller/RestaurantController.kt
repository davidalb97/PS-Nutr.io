package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.dataAccess.RestaurantDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbRestaurantRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantResponse
import java.util.concurrent.CompletableFuture

const val MAX_RADIUS = 1000

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
        private val dbRestaurantRepository: DbRestaurantRepository,
        private val restaurantApiRepository: RestaurantApiRepository
) {

    @GetMapping
    fun getNearbyRestaurants(latitude: Float?, longitude: Float?, radius: Int? = MAX_RADIUS, apiType: String?): String {
        return if (latitude == null || longitude == null) "emptySet()" else {
            val chosenRadius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS

            val restaurantApi = restaurantApiRepository.getRestaurantApi(apiType)

            val apiRestaurants = CompletableFuture
                    .supplyAsync { restaurantApi.searchRestaurants(latitude, longitude, chosenRadius) }
                    .thenApply { it.map(RestaurantDtoMapper::mapDto) }

            val dbRestaurants = dbRestaurantRepository
                    .getRestaurantsByCoordinates(latitude, longitude, chosenRadius)
                    .map(RestaurantDtoMapper::mapDto)

            filterRedundantApiRestaurants(dbRestaurants, apiRestaurants.get()).forEach { println(it.apiId) }
            return ""
        }
    }

    private fun dbContainsRestaurant(dbRestaurants: List<RestaurantResponse>, restaurant: RestaurantResponse): Boolean {
        return dbRestaurants.any { dbRestaurant -> restaurant.apiId == dbRestaurant.apiId }
    }

    /**
     * Obtain more information for a Restaurant with given id.
     *
     * Current search algorithm will first query the Database for any restaurant, and only if none was found,
     * search the preferred Restaurant API (Zomato, etc.)
     */
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRestaurantInformation(@PathVariable id: Int, api: String?): String {
        return ""
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRestaurant(@RequestBody restaurant: RestaurantInput) {
    }

    @DeleteMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRestaurant(@PathVariable id: String) {

    }

    @PostMapping("/{id}/report", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantReport(@PathVariable id: String, @RequestBody report: String) {

    }

    @PostMapping("/{id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantVote(@PathVariable id: String, @RequestBody vote: String) {

    }

    @PutMapping("/{id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRestaurantVote(@PathVariable id: String, @RequestBody vote: String) {

    }

    @DeleteMapping("/{id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRestaurantVote(@PathVariable id: String, vote: String) {

    }

    private fun filterRedundantApiRestaurants(dbRestaurants: List<RestaurantResponse>, apiRestaurants: List<RestaurantResponse>): List<RestaurantResponse> {
        val result = dbRestaurants.toMutableList()

        apiRestaurants.forEach {
            if (!dbContainsRestaurant(result, it)) {
                result.add(it)
            }
        }
        return result
    }
}