package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.input.RestaurantInput
import pt.isel.ps.g06.httpserver.dataAccess.input.VoteInput
import pt.isel.ps.g06.httpserver.dataAccess.model.SimplifiedRestaurantOutputModel
import pt.isel.ps.g06.httpserver.dataAccess.model.toSimplifiedRestaurant
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.service.RestaurantService
import java.net.URI
import javax.validation.Valid

private const val INVALID_RESTAURANT_SEARCH = "To search nearby restaurants, a geolocation must be given!"

@Suppress("MVCPathVariableInspection") //False positive for IntelliJ
@RestController
@RequestMapping(RESTAURANTS, produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class RestaurantController(private val restaurantService: RestaurantService) {
    /**
     * Allows to search for Restaurants from both an API (see [RestaurantApiType] for supported APIs) and
     * from a database around geolocation,
     * giving priority to database values, as they may contain extra information added by an user.
     *
     * @param name optional, filters results by restaurant name
     * @param radius optional, changes the search radius in a circle (meters).
     *  @param apiType allows the user to select which API to search from. See [RestaurantApiType].
     */
    @GetMapping(consumes = [MediaType.ALL_VALUE])
    fun searchRestaurants(
            latitude: Float?,
            longitude: Float?,
            name: String?,
            radius: Int?,
            apiType: String?
    ): Collection<SimplifiedRestaurantOutputModel> {
        if (latitude == null || longitude == null) {
            throw InvalidInputException(InvalidInputDomain.SEARCH_RESTAURANT, INVALID_RESTAURANT_SEARCH)
        }

        val nearbyRestaurants = restaurantService.getNearbyRestaurants(
                latitude = latitude,
                longitude = longitude,
                name = name,
                radius = radius,
                apiType = apiType
        )

        return nearbyRestaurants.map { toSimplifiedRestaurant(it) }
    }

    @GetMapping(RESTAURANT, consumes = [MediaType.ALL_VALUE])
    fun getRestaurantInformation(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestParam api: String?): Restaurant {
        return restaurantService.getRestaurant(id, api) ?: throw RestaurantNotFoundException()
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRestaurant(@Valid @RequestBody restaurantInput: RestaurantInput): ResponseEntity<Void> {
        //TODO - Right now cuisines aren't being validated to make sure they exist in the database
        val createdRestaurant = restaurantService.createRestaurant(restaurantInput)

        return ResponseEntity
                .created(URI("$RESTAURANTS/${createdRestaurant.submission_id}"))
                .build()
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