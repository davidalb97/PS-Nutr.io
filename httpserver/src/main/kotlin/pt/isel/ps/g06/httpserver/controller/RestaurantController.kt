package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.dataAccess.api.ZomatoApi
import pt.isel.ps.g06.httpserver.dataAccess.database.model.DbRestaurant
import pt.isel.ps.g06.httpserver.dataAccess.database.repos.RestaurantsRepository

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
        private val restaurantsRepository: RestaurantsRepository,
        private val zomatoApi: ZomatoApi
) {

    @GetMapping
    fun restaurantsHandler(latitude: Float, longitude: Float): Set<DbRestaurant> {
        val restaurantsDb = restaurantsRepository.getRestaurantsByCoordinates(latitude, longitude)
        val restaurantsApi = zomatoApi.searchRestaurants(latitude, longitude)
                .restaurants.map {
                    DbRestaurant(
                            it.restaurant.id,
                            it.restaurant.name,
                            it.restaurant.location.latitude,
                            it.restaurant.location.longitude
                    )
                }
        return restaurantsDb.union(restaurantsApi)
    }
}