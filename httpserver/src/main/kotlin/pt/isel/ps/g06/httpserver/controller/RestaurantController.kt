package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.dataAccess.api.ZomatoApi
import pt.isel.ps.g06.httpserver.dataAccess.api.dtos.RestaurantContainerDto
import pt.isel.ps.g06.httpserver.dataAccess.api.dtos.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.dtos.RestaurantSearchResultDto
import pt.isel.ps.g06.httpserver.dataAccess.database.model.DbRestaurant
import pt.isel.ps.g06.httpserver.dataAccess.database.repos.RestaurantsRepository
import pt.isel.ps.g06.httpserver.model.BaseRestaurant
import pt.isel.ps.g06.httpserver.model.mapToSiren
import java.util.concurrent.CompletableFuture

const val MAX_RADIUS = 1000

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
        private val restaurantsRepository: RestaurantsRepository,
        private val zomatoApi: ZomatoApi
) {

    @GetMapping
    fun restaurantsHandler(latitude: Float?, longitude: Float?, inRadius: Int? = MAX_RADIUS): List<BaseRestaurant> {
        return (if (latitude == null || longitude == null) emptySet() else {
            val radius = if (inRadius != null && inRadius <= MAX_RADIUS) inRadius else MAX_RADIUS

            CompletableFuture
                    .supplyAsync { restaurantsRepository.getRestaurantsByCoordinates(latitude, longitude, radius) }
                    .thenApply { it.map { dto -> mapToSiren(dto) } }
                    .thenCombine(zomatoApi.searchRestaurants(latitude, longitude, radius)) { first, second ->
                        val list = second.restaurants.map { dto -> mapToSiren(dto.restaurant) }.toMutableList()
                        list.addAll(first)
                        return list
                    }
        })
    }
}