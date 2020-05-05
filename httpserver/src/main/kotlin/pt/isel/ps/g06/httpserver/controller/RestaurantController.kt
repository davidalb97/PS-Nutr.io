package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.RestaurantFilter
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.data.ReportInput
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.service.RestaurantService
import pt.isel.ps.g06.httpserver.data.VoteInput
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbReportRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbRestaurantRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbVotableRepository

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

    // TODO
    @PostMapping()
    fun createRestaurant(@RequestBody restaurant: RestaurantInput) {
        dbRestaurantRepository.insertRestaurant(
                restaurant.submitterId,
                restaurant.name,
                null,
                emptyList(),
                restaurant.latitude,
                restaurant.longitude,
                null
        )
    }

    // TODO
    @DeleteMapping(RESTAURANT)
    fun deleteRestaurant(@PathVariable(RESTAURANT_ID_VALUE) id: Int) =
            dbRestaurantRepository.deleteRestaurant(1, id)

    }

    @PostMapping(RESTAURANT_REPORT)
    fun addRestaurantReport(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody report: String) {

    }

    @PostMapping(RESTAURANT_VOTE)
    fun addRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody vote: String) {

    }

    @PutMapping(RESTAURANT_VOTE)
    fun updateRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody vote: String) {

    // TODO
    @PutMapping("/{submission_id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRestaurantVote(@PathVariable submission_id: Int, @RequestBody vote: VoteInput) =
            dbVotableRepository.updateVote(vote.submitterId, submission_id, vote.value)

    @DeleteMapping(RESTAURANT_VOTE)
    fun deleteRestaurantVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, vote: String) {

    }
}