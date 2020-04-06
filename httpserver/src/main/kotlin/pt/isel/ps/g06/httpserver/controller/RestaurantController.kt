package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.dataAccess.api.ZomatoApi
import pt.isel.ps.g06.httpserver.dataAccess.database.repos.RestaurantsRepository

const val MAX_RADIUS = 1000

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
        private val restaurantsRepository: RestaurantsRepository,
        private val zomatoApi: ZomatoApi
) {

    @GetMapping
    fun getNearbyRestaurants(latitude: Float?, longitude: Float?, inRadius: Int? = MAX_RADIUS): String {
        return ""
    }

    @GetMapping("/{id}")
    fun getRestaurantInformation(@PathVariable id: String): String {
        return ""
    }

    @PostMapping(consumes = ["application/json"])
    fun createRestaurant(@RequestBody restaurant: RestaurantInput) {

    }

    @PostMapping("/{id}/report", consumes = ["application/json"])
    fun addRestaurantReport(@PathVariable id: String, @RequestBody report: String) {

    }

    @PostMapping("/{id}/vote", consumes = ["application/json"])
    fun addRestaurantVote(@PathVariable id: String, @RequestBody vote: String) {

    }

    @PutMapping("/{id}/vote", consumes = ["application/json"])
    fun updateRestaurantVote(@PathVariable id: String, @RequestBody vote: String) {

    }

    @DeleteMapping("/{id}", consumes = ["application/json"])
    fun deleteRestaurant(@PathVariable id: String) {

    }

    @DeleteMapping("/{id}/vote", consumes = ["application/json"])
    fun deleteRestaurantVote(@PathVariable id: String, vote: String) {

    }

//        return (if (latitude == null || longitude == null) emptySet() else {
//            val radius = if (inRadius != null && inRadius <= MAX_RADIUS) inRadius else MAX_RADIUS
//
//            CompletableFuture
//                    .supplyAsync { restaurantsRepository.getRestaurantsByCoordinates(latitude, longitude, radius) }
//                                .thenApply { it.map { dto -> mapToSiren(dto) } }
//                                .thenCombine(zomatoApi.searchRestaurants(latitude, longitude, radius)) { first, second ->
//                                    val list = second.restaurants.map { dto -> mapToSiren(dto.restaurant) }.toMutableList()
//                                    list.addAll(first)
//                        return list
//                    }
//        })
//    }
}