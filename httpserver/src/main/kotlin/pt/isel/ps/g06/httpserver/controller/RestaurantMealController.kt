package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.common.exception.authorization.NotAuthorizedException
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.input.*
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.DetailedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.RestaurantMealContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toDetailedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toRestaurantMealContainerOutput
import pt.isel.ps.g06.httpserver.model.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.RestaurantMealService
import pt.isel.ps.g06.httpserver.service.RestaurantService
import pt.isel.ps.g06.httpserver.service.SubmissionService
import javax.validation.Valid

// TODO add report for restaurant meal
@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class RestaurantMealController(
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val submissionService: SubmissionService,
        private val restaurantMealService: RestaurantMealService,
        private val restaurantIdentifierBuilder: RestaurantIdentifierBuilder
) {
    @GetMapping(RESTAURANT_MEALS, consumes = [MediaType.ALL_VALUE])
    fun getMealsForRestaurant(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            count: Int?,
            skip: Int?,
            user: User?
    ): ResponseEntity<RestaurantMealContainerOutput> {
        val (submitterId, submissionId, apiId) = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        val restaurant = restaurantService
                .getRestaurant(submitterId, submissionId, apiId)
                ?: throw RestaurantNotFoundException()

        return ResponseEntity
                .ok()
                .body(toRestaurantMealContainerOutput(restaurant, user?.identifier))
    }


    @GetMapping(RESTAURANT_MEAL, consumes = [MediaType.ALL_VALUE])
    fun getRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            user: User?
    ): ResponseEntity<DetailedRestaurantMealOutput> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        val restaurantMeal = restaurantMealService.getRestaurantMeal(restaurantIdentifier, mealId)

        return ResponseEntity
                .ok()
                .body(toDetailedRestaurantMealOutput(restaurantMeal, user?.identifier))
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
            @PathVariable(RESTAURANT_ID_VALUE) id: String,
            @Valid @RequestBody restaurantMeal: RestaurantMealInput,
            user: User
    ): ResponseEntity<Void> {
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

        restaurantMealService.addRestaurantMeal(restaurantIdentifier, meal, user.identifier)

        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromUriString(RESTAURANT_MEAL)
                        .buildAndExpand(restaurant.identifier.value.toString(), meal.identifier)
                        .toUri())
                .build()
    }


    @PostMapping(RESTAURANT_MEAL_PORTION)
    fun addMealPortion(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody portion: PortionInput,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.addRestaurantMealPortion(
                restaurantId = restaurantIdentifier,
                mealId = mealId,
                submitterId = user.identifier,
                quantity = portion.quantity!!
        )

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_MEAL_VOTE)
    fun alterRestaurantMealVote(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody userVote: VoteInput,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)
        val restaurantMeal = restaurantMealService.getOrAddRestaurantMeal(restaurantIdentifier, mealId, user.identifier)


        submissionService.alterRestaurantMealVote(
                mealRestaurantInfo = restaurantMeal,
                submitterId = user.identifier,
                voteState = userVote.vote
        )

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_MEAL)
    fun putVerifyRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @RequestBody verified: Boolean,
            user: User
    ): ResponseEntity<Void> {
        // Get restaurant ID
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)
        // Get restaurant's meal ID
        val restaurantMeal = restaurantMealService.getRestaurantMeal(restaurantIdentifier, mealId)
        // Check if the submitter is the restaurant owner
        val isOwner = restaurantService.getRestaurant(restaurantIdentifier)?.ownerId == user.identifier

        if (!isOwner) {
            throw NotAuthorizedException()
        }

        // Put/remove restaurant meal's verification
        restaurantMealService.updateRestaurantMealVerification(restaurantIdentifier, mealId, verified)

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_MEAL_REPORT)
    fun addReport(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @RequestBody reportInput: ReportInput,
            user: User
    ): ResponseEntity<Void> {

        val restaurantIdentifier: RestaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.addReport(user.identifier, restaurantIdentifier, mealId, reportInput.description)

        return ResponseEntity
                .ok()
                .build()
    }

    @DeleteMapping(RESTAURANT_MEAL_PORTION)
    fun deleteMealPortion(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteUserPortion(restaurantIdentifier, mealId, user.identifier)

        return ResponseEntity
                .ok()
                .build()
    }

    @DeleteMapping(RESTAURANT_MEAL)
    fun deleteRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteRestaurantMeal(restaurantIdentifier, mealId, user.identifier)

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_MEAL_FAVORITE)
    fun setFavoriteRestaurant(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody favorite: FavoriteInput,
            user: User
    ): ResponseEntity<Any> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.setFavorite(
                restaurantIdentifier,
                mealId,
                user.identifier,
                favorite.isFavorite!!
        )
        return ResponseEntity.ok().build()
    }
}
