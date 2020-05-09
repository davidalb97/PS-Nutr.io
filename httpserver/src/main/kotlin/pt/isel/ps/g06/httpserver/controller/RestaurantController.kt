package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.data.VoteInput
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.model.RestaurantApiType
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.service.RestaurantService

private const val INVALID_RESTAURANT_SEARCH = "To search nearby restaurants, either a geolocation or a name must be given!"

@Suppress("MVCPathVariableInspection") //False positive for IntelliJ
@RestController
@RequestMapping(RESTAURANTS, produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class RestaurantController(private val restaurantService: RestaurantService) {

    /**
     * Allows to search for Restaurants from both an API (see [RestaurantApiType] for supported APIs) and
     * from a database, giving priority to database values, as they may contain extra information added by an user.
     *
     * Search can be done by either providing a geolocation:
     * @param name optional, filters geolocation result by restaurant name
     * @param radius optional, reduces the search radius
     * -----
     * Or simply by giving a restaurant name and a country, to better identify where to search.
     * @param name the restaurant name
     * @param country an ISO 3166-1 alpha-3 Language Code. See [documentation.](https://www.iso.org/iso-3166-country-codes.html)
     *  -----
     *  Other optional parameters:
     *  @param apiType allows the user to select which API to search from. See [RestaurantApiType].
     */
    @GetMapping(consumes = [MediaType.ALL_VALUE])
    fun searchRestaurants(
            latitude: Float?,
            longitude: Float?,
            name: String?,
            country: String?,
            radius: Int?,
            apiType: String?
    ): Set<Restaurant> {
        if (latitude == null || longitude == null) {
            if (name != null) {
                return restaurantService.searchRestaurantsByName(
                        name = name,
                        countryCode = country,
                        apiType = apiType
                )
            } else throw InvalidInputException(InvalidInputDomain.SEARCH_RESTAURANT, INVALID_RESTAURANT_SEARCH)
        }

        return restaurantService.getNearbyRestaurants(
                latitude = latitude,
                longitude = longitude,
                name = name,
                radius = radius,
                apiType = apiType
        )
    }

    @GetMapping(RESTAURANT, consumes = [MediaType.ALL_VALUE])
    fun getRestaurantInformation(@PathVariable(RESTAURANT_ID_VALUE) id: String, api: String?): Restaurant {
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