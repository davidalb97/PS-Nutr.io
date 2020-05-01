package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.RestaurantFilter
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.service.RestaurantService

@Suppress("MVCPathVariableInspection") //False positive for IntelliJ
@RestController
@RequestMapping(
        RESTAURANTS,
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
)
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

    @GetMapping(RESTAURANT)
    fun getRestaurantInformation(@PathVariable(RESTAURANT_ID_VALUE) id: Int, api: String?): Restaurant {
        return restaurantService.getRestaurant(id, api) ?: throw RestaurantNotFoundException()
    }

    @PostMapping()
    fun createRestaurant(@RequestBody restaurant: RestaurantInput) {
    }

    @DeleteMapping(RESTAURANT)
    fun deleteRestaurant(@PathVariable(RESTAURANT_ID_VALUE) id: String) {

    }

    @PostMapping(RESTAURANT_REPORT)
    fun addRestaurantReport(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody report: String) {

    }

    @PostMapping(RESTAURANT_VOTE)
    fun addRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody vote: String) {

    }

    @PutMapping(RESTAURANT_VOTE)
    fun updateRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody vote: String) {

    }

    @DeleteMapping(RESTAURANT_VOTE)
    fun deleteRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, vote: String) {

    }
}