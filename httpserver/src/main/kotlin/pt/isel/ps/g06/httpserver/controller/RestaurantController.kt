package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidInputException
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.input.*
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.DetailedRestaurantOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.SimplifiedRestaurantOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.toDetailedRestaurantOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.toSimplifiedRestaurantOutput
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.RestaurantService
import pt.isel.ps.g06.httpserver.service.SubmissionService
import pt.isel.ps.g06.httpserver.service.UserService
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

private const val INVALID_RESTAURANT_SEARCH = "To search nearby restaurants, a geolocation must be given!"

@Validated
@Suppress("MVCPathVariableInspection") //False positive for IntelliJ
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class RestaurantController(
        private val restaurantService: RestaurantService,
        private val submissionService: SubmissionService,
        private val userService: UserService,
        private val authenticationService: AuthenticationService,
        private val restaurantIdentifierBuilder: RestaurantIdentifierBuilder
) {
    /**
     * Allows to search for Restaurants from both an API (see [RestaurantApiType] for supported APIs) and
     * from a database around geolocation,
     * giving priority to database values as they may contain extra information added by an user.
     *
     * @param name optional, filters results by restaurant name
     * @param radius optional, changes the search radius in a circle (meters).
     * @param apiType allows the user to select which API to search from. See [RestaurantApiType].
     */
    @GetMapping(RESTAURANTS, consumes = [MediaType.ALL_VALUE])
    fun searchRestaurants(
            @RequestParam latitude: Float?,
            @RequestParam longitude: Float?,
            @RequestParam name: String?,
            @RequestParam radius: Int?,
            @RequestParam apiType: String?,
            @RequestParam @Min(0) skip: Int?,
            @RequestParam(defaultValue = DEFAULT_COUNT_STR) @Min(0) @Max(MAX_COUNT) count: Int?,
            user: User?
    ): ResponseEntity<Collection<SimplifiedRestaurantOutput>> {
        if (latitude == null || longitude == null) {
            throw InvalidInputException(INVALID_RESTAURANT_SEARCH)
        }

        val nearbyRestaurants = restaurantService.getNearbyRestaurants(
                latitude = latitude,
                longitude = longitude,
                name = name,
                radius = radius,
                apiType = apiType,
                skip = skip,
                count = count
        )
        return ResponseEntity
                .ok()
                .body(nearbyRestaurants.map { toSimplifiedRestaurantOutput(it, user?.identifier) }.toList())
    }

    @GetMapping(RESTAURANT, consumes = [MediaType.ALL_VALUE])
    fun getRestaurantInformation(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            user: User?
    ): ResponseEntity<DetailedRestaurantOutput> {
        val restaurant = ensureRestaurantExists(restaurantId)

        return ResponseEntity
                .ok()
                .body(toDetailedRestaurantOutput(restaurant, user?.identifier))
    }

    @PostMapping(RESTAURANTS, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRestaurant(
            @Valid @RequestBody restaurant: RestaurantInput,
            user: User
    ): ResponseEntity<Void> {

        val createdRestaurant = restaurantService.createRestaurant(
                submitterId = user.identifier,
                restaurantName = restaurant.name!!,
                cuisines = restaurant.cuisines!!,
                latitude = restaurant.latitude!!,
                longitude = restaurant.longitude!!,
                ownerId = restaurant.ownerId
        )

        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromUriString(RESTAURANT)
                        .buildAndExpand(createdRestaurant.identifier.value.toString())
                        .toUri())
                .build()
    }

    @DeleteMapping(RESTAURANT, consumes = [MediaType.ALL_VALUE])
    fun deleteRestaurant(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            user: User
    ): ResponseEntity<Void> {

        val restaurant = ensureRestaurantExists(restaurantId)

        if (!restaurant.isPresentInDatabase()) {
            //If Restaurant is not in database, owner is an API
            throw NotSubmissionOwnerException()
        }

        submissionService.deleteSubmission(restaurant.identifier.value.submissionId!!, user)

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_VOTE, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun putRestaurantVote(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @Valid @RequestBody userVote: VoteInput,
            user: User
    ): ResponseEntity<Void> {

        val restaurant = ensureRestaurantExists(restaurantId)

        submissionService.alterRestaurantVote(
                restaurant = restaurant,
                submitterId = user.identifier,
                voteState = userVote.vote
        )

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_FAVORITE)
    fun setFavoriteRestaurant(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @Valid @RequestBody favorite: FavoriteInput,
            user: User
    ): ResponseEntity<Any> {

        var restaurant = ensureRestaurantExists(restaurantId)
        restaurant = restaurantService.createRestaurantIfAbsent(restaurant)

        restaurantService.setFavorite(
                restaurant.identifier.value.submissionId!!,
                user.identifier,
                favorite.isFavorite!!
        )
        return ResponseEntity.ok().build()
    }

    @PutMapping(RESTAURANT_REPORT)
    fun addReport(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @RequestBody reportInput: ReportInput,
            user: User
    ): ResponseEntity<Void> {

        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantService.addReport(user.identifier, restaurantIdentifier, reportInput.description)

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT)
    fun addOwner(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @RequestBody restaurantOwnerInput: RestaurantOwnerInput,
            user: User
    ): ResponseEntity<Void> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        val restaurantId = ensureRestaurantExists(restaurantId).identifier.value.submissionId!!

        restaurantService.addOwner(restaurantId, restaurantOwnerInput.ownerId)

        return ResponseEntity.ok().build()
    }

    /**
     * Helper method to avoid boilerplate code that ensures that a Restaurant exists.
     *
     * @throws RestaurantNotFoundException if it doesn't.
     */
    private fun ensureRestaurantExists(restaurantId: String): Restaurant {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        return restaurantService
                .getRestaurant(restaurantIdentifier)
                ?: throw RestaurantNotFoundException()
    }
}