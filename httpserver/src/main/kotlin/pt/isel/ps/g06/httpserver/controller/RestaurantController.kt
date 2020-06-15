package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.input.RestaurantInput
import pt.isel.ps.g06.httpserver.dataAccess.input.VoteInput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.DetailedRestaurantOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.SimplifiedRestaurantOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.toDetailedRestaurantOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.toSimplifiedRestaurantOutput
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.service.RestaurantService
import pt.isel.ps.g06.httpserver.service.SubmissionService
import javax.validation.Valid

private const val INVALID_RESTAURANT_SEARCH = "To search nearby restaurants, a geolocation must be given!"

@Suppress("MVCPathVariableInspection") //False positive for IntelliJ
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class RestaurantController(
        private val restaurantService: RestaurantService,
        private val submissionService: SubmissionService,
        private val restaurantIdentifierBuilder: RestaurantIdentifierBuilder
) {
    /**
     * Allows to search for Restaurants from both an API (see [RestaurantApiType] for supported APIs) and
     * from a database around geolocation,
     * giving priority to database values, as they may contain extra information added by an user.
     *
     * @param name optional, filters results by restaurant name
     * @param radius optional, changes the search radius in a circle (meters).
     *  @param apiType allows the user to select which API to search from. See [RestaurantApiType].
     */
    @GetMapping(RESTAURANTS, consumes = [MediaType.ALL_VALUE])
    fun searchRestaurants(
            latitude: Float?,
            longitude: Float?,
            name: String?,
            radius: Int?,
            apiType: String?
    ): ResponseEntity<Collection<SimplifiedRestaurantOutput>> {
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

        return ResponseEntity
                .ok()
                .body(nearbyRestaurants.map { toSimplifiedRestaurantOutput(it) }.toList())
    }

    @GetMapping(RESTAURANT, consumes = [MediaType.ALL_VALUE])
    fun getRestaurantInformation(
            @PathVariable(RESTAURANT_ID_VALUE) id: String
    ): ResponseEntity<DetailedRestaurantOutput> {
        val (submitterId, submissionId, apiId) = restaurantIdentifierBuilder.extractIdentifiers(id)

        val restaurant = restaurantService.getRestaurant(
                submitterId = submitterId,
                submissionId = submissionId,
                apiId = apiId
        ) ?: throw RestaurantNotFoundException()

        return ResponseEntity
                .ok()
                .body(toDetailedRestaurantOutput(restaurant))
    }

    @PostMapping(RESTAURANTS, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRestaurant(@Valid @RequestBody restaurant: RestaurantInput): ResponseEntity<Void> {
        val submitterId = 3        //TODO For when there's authentication

        val createdRestaurant = restaurantService.createRestaurant(
                submitterId = submitterId,
                restaurantName = restaurant.name!!,
                cuisines = restaurant.cuisines!!,
                latitude = restaurant.latitude!!,
                longitude = restaurant.longitude!!
        )

        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromUriString(RESTAURANT)
                        .buildAndExpand(createdRestaurant.identifier.value.toString())
                        .toUri())
                .build()
    }

    @DeleteMapping(RESTAURANT, consumes = [MediaType.ALL_VALUE])
    fun deleteRestaurant(@PathVariable(RESTAURANT_ID_VALUE) id: String): ResponseEntity<Void> {
        //TODO Authentication
        val userId = 3
        val (submitterId, submissionId, apiId) = restaurantIdentifierBuilder.extractIdentifiers(id)

        val restaurant = (restaurantService
                .getRestaurant(submitterId, submissionId, apiId)
                ?: throw RestaurantNotFoundException())


        if (!restaurant.isPresentInDatabase() || restaurant.creatorInfo.value.identifier != userId) {
            //If Restaurant is not in database, owner is an API
            throw NotSubmissionOwnerException()
        }

        submissionService.deleteSubmission(restaurant.identifier.value.submissionId!!, userId)

        return ResponseEntity.ok().build()
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