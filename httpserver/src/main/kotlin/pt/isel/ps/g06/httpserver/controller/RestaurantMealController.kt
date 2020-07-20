package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.input.PortionInput
import pt.isel.ps.g06.httpserver.dataAccess.input.RestaurantMealInput
import pt.isel.ps.g06.httpserver.dataAccess.input.VoteInput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.DetailedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.RestaurantMealContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toDetailedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toRestaurantMealContainerOutput
import pt.isel.ps.g06.httpserver.service.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class RestaurantMealController(
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val submissionService: SubmissionService,
        private val restaurantMealService: RestaurantMealService,
        private val userService: UserService,
        private val restaurantIdentifierBuilder: RestaurantIdentifierBuilder,
        private val authenticationService: AuthenticationService
) {
    @GetMapping(RESTAURANT_MEALS, consumes = [MediaType.ALL_VALUE])
    fun getMealsForRestaurant(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String
    ): ResponseEntity<RestaurantMealContainerOutput> {
        val (submitterId, submissionId, apiId) = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        val restaurant = restaurantService
                .getRestaurant(submitterId, submissionId, apiId)
                ?: throw RestaurantNotFoundException()

        return ResponseEntity
                .ok()
                .body(toRestaurantMealContainerOutput(restaurant))
    }


    @GetMapping(RESTAURANT_MEAL, consumes = [MediaType.ALL_VALUE])
    fun getRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int
    ): ResponseEntity<DetailedRestaurantMealOutput> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        val restaurantMeal = restaurantMealService.getRestaurantMeal(restaurantIdentifier, mealId)

        return ResponseEntity
                .ok()
                .body(toDetailedRestaurantMealOutput(restaurantMeal))
    }


    /**
     *  Adds an existing User created meal to an existing Restaurant, allowing other
     *  users to see it, add portions, vote and report it.
     *
     *  Internally and as a side effect, if a Restaurant exists
     *  but is not present in the database, it is first inserted into the database.
     *
     *  This logic does not change the client's behavior on accessing that restaurant.
     */
    @PutMapping(RESTAURANT_MEALS, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantMeal(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(RESTAURANT_ID_VALUE) id: String,
            @Valid @RequestBody restaurantMeal: RestaurantMealInput,
            request: HttpServletRequest
    ): ResponseEntity<Void> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(id)

        val restaurant = restaurantService
                .getRestaurant(restaurantIdentifier)
                ?: throw RestaurantNotFoundException()

        val meal = mealService
                .getMeal(restaurantMeal.mealId!!)
                ?: throw MealNotFoundException()

        if (!meal.isUserMeal()) {
            throw NotSubmissionOwnerException("Only meals created by you can be inserted!")
        }

        restaurantMealService.addRestaurantMeal(restaurantIdentifier, meal, submitterId)

        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromUriString(RESTAURANT_MEAL)
                        .buildAndExpand(restaurant.identifier.value.toString(), meal.identifier)
                        .toUri())
                .build()
    }


    @PostMapping(RESTAURANT_MEAL_PORTION)
    fun addMealPortion(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody portion: PortionInput,
            request: HttpServletRequest
    ): ResponseEntity<Void> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.addRestaurantMealPortion(
                restaurantId = restaurantIdentifier,
                mealId = mealId,
                submitterId = submitterId,
                quantity = portion.quantity!!
        )

        return ResponseEntity.ok().build()
    }

    @PutMapping(RESTAURANT_MEAL_VOTE)
    fun alterRestaurantMealVote(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody userVote: VoteInput,
            request: HttpServletRequest
    ): ResponseEntity<Void> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)
        val restaurantMeal = restaurantMealService.getRestaurantMeal(restaurantIdentifier, mealId)

        submissionService.alterRestaurantMealVote(
                restaurantMeal = restaurantMeal,
                submitterId = submitterId,
                vote = userVote.vote!!
        )

        return ResponseEntity.ok().build()
    }

    @DeleteMapping(RESTAURANT_MEAL_VOTE)
    fun deleteMealVote(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            request: HttpServletRequest
    ): ResponseEntity<Void> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)
        val restaurantMeal = restaurantMealService.getRestaurantMeal(restaurantIdentifier, mealId)

        submissionService.deleteRestaurantMealVote(
                restaurantMeal = restaurantMeal,
                submitterId = submitterId
        )

        return ResponseEntity.ok().build()
    }

    @DeleteMapping(RESTAURANT_MEAL_PORTION)
    fun deleteMealPortion(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            request: HttpServletRequest
    ): ResponseEntity<Void> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteUserPortion(restaurantIdentifier, mealId, submitterId)

        return ResponseEntity.ok().build()
    }

    @DeleteMapping(RESTAURANT_MEAL)
    fun deleteRestaurantMeal(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            request: HttpServletRequest
    ): ResponseEntity<Void> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteRestaurantMeal(restaurantIdentifier, mealId, submitterId)

        return ResponseEntity.ok().build()
    }
}
