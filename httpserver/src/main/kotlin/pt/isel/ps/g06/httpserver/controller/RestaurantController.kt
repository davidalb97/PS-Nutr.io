package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbRestaurantRepository

const val MAX_RADIUS = 1000

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
        private val dbRestaurantRepository: DbRestaurantRepository,
        private val restaurantApiRepository: RestaurantApiRepository
) {

    @GetMapping
    fun getNearbyRestaurants(latitude: Float?, longitude: Float?, radius: Int? = MAX_RADIUS): String {
        return if (latitude == null || longitude == null) "emptySet()" else {
            val radius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS

            //Get from database and chosen API (if any)
            //Distinct those that aren't equal in both API and DB (how? By Db_api_id)
            //map to response and send it

            return ""
        }

//
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
}