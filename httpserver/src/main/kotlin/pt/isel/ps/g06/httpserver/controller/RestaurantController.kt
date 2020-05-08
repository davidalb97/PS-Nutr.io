package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.data.VoteInput
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.service.RestaurantService

@Suppress("MVCPathVariableInspection") //False positive for IntelliJ
@RestController
@RequestMapping(RESTAURANTS, produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class RestaurantController(private val restaurantService: RestaurantService) {

    @GetMapping(consumes = [MediaType.ALL_VALUE])
    fun getNearbyRestaurants(
            latitude: Float?,
            longitude: Float?,
            radius: Int?,
//            cuisines: List<String>? = emptyList(),
//            meals: List<String>? = emptyList(),
            apiType: String?
    ): Set<Restaurant> {
        return restaurantService.getNearbyRestaurants(latitude, longitude, radius, apiType)
//        return RestaurantFilter(nearbyRestaurants).filter(cuisines, meals).toSet()
    }

    @GetMapping(RESTAURANT, consumes = [MediaType.ALL_VALUE])
    fun getRestaurantInformation(@PathVariable(RESTAURANT_ID_VALUE) id: Int, api: String?): Restaurant {
        return restaurantService.getRestaurant(id, api) ?: throw RestaurantNotFoundException()
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRestaurant(@RequestBody restaurant: RestaurantInput) {

    }

    @DeleteMapping(RESTAURANT, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRestaurant(@PathVariable(RESTAURANT_ID_VALUE) id: Int) {
    }

    @PostMapping(RESTAURANT_REPORT, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantReport(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody report: String) {

    }

    @PostMapping(RESTAURANT_VOTE, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody vote: String) {

    }

    @PutMapping(RESTAURANT_VOTE, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody vote: String) {

    }

    @PutMapping("/{submission_id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRestaurantVote(@PathVariable submission_id: Int, @RequestBody vote: VoteInput) {
    }

    @DeleteMapping(RESTAURANT_VOTE, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, vote: String) {
    }
}