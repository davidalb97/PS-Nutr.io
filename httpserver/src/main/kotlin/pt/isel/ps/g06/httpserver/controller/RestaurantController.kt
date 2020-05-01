package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.RestaurantFilter
import pt.isel.ps.g06.httpserver.common.exception.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.service.RestaurantService

@RestController
@RequestMapping("/restaurant")
class RestaurantController(private val restaurantService: RestaurantService) {

    @GetMapping
    fun getNearbyRestaurants(
            latitude: Float?,
            longitude: Float?,
            radius: Int?,
            cuisines: List<String> = emptyList(),
            meals: List<String> = emptyList(),
            apiType: String?
    ): Set<Restaurant> {
        val nearbyRestaurants = restaurantService.getNearbyRestaurants(latitude, longitude, radius, apiType)
        return RestaurantFilter(nearbyRestaurants).filter(cuisines, meals).toSet()
    }


    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRestaurantInformation(@PathVariable id: Int, api: String?): Restaurant {
        return restaurantService.getRestaurant(id, api) ?: throw RestaurantNotFoundException()
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