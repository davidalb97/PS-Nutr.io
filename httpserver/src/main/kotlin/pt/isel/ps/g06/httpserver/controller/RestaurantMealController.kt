package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.forbidden.BaseForbiddenException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.input.moderation.VerifyInput
import pt.isel.ps.g06.httpserver.dataAccess.input.restaurantMeal.PortionInput
import pt.isel.ps.g06.httpserver.dataAccess.input.restaurantMeal.RestaurantMealInput
import pt.isel.ps.g06.httpserver.dataAccess.input.userActions.FavoriteInput
import pt.isel.ps.g06.httpserver.dataAccess.input.userActions.ReportInput
import pt.isel.ps.g06.httpserver.dataAccess.input.userActions.VoteInput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal.DetailedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal.RestaurantMealsContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal.toDetailedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal.toRestaurantMealsContainerOutput
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.RestaurantMealService
import pt.isel.ps.g06.httpserver.service.RestaurantService
import pt.isel.ps.g06.httpserver.service.SubmissionService
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

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
    @GetMapping(RESTAURANT_MEALS_PATH, consumes = [MediaType.ALL_VALUE])
    fun getMealsForRestaurant(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @Min(0) skip: Int?,
            @Min(0) @Max(MAX_COUNT) count: Int?,
            user: User?
    ): ResponseEntity<RestaurantMealsContainerOutput> {
        val (submitterId, submissionId, apiId) = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        val restaurant = restaurantService
                .getRestaurant(submitterId, submissionId, apiId)
                ?: throw RestaurantNotFoundException()

        return ResponseEntity
                .ok()
                .body(toRestaurantMealsContainerOutput(restaurant, user?.identifier))
    }


    @GetMapping(RESTAURANT_MEAL_ID_PATH, consumes = [MediaType.ALL_VALUE])
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
    @PostMapping(RESTAURANT_MEALS_PATH, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) id: String,
            @Valid @RequestBody restaurantMeal: RestaurantMealInput,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(id)

        val restaurant = restaurantService
                .getRestaurant(restaurantIdentifier)
                ?: throw RestaurantNotFoundException()

        val meal = mealService.getMeal(restaurantMeal.mealId!!)
                ?: throw MealNotFoundException()

        //TODO check with group
        //This allows for any favorite/custom/suggested meal to be inserted
        if (meal.type == MealType.SUGGESTED_INGREDIENT) {
            throw InvalidInputException("Cannot insert ingredients!")
        }

        restaurantMealService.addRestaurantMeal(restaurantIdentifier, meal, user.identifier)

        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromUriString(RESTAURANT_MEAL_ID_PATH)
                        .buildAndExpand(restaurant.identifier.value.toString(), meal.identifier)
                        .toUri())
                .build()
    }


    @PostMapping(RESTAURANT_MEAL_PORTION_PATH)
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

    @PutMapping(RESTAURANT_MEAL_VOTE_PATH)
    fun alterRestaurantMealVote(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody userVote: VoteInput,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)
        val restaurantMeal = restaurantMealService.getOrAddRestaurantMeal(restaurantIdentifier, mealId)

        submissionService.alterRestaurantMealVote(
                restaurantMealId = restaurantMeal.info?.identifier!!,
                submitterId = user.identifier,
                voteState = userVote.vote
        )

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_MEAL_ID_PATH)
    fun putVerifyRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @RequestBody verifyInput: VerifyInput,
            user: User
    ): ResponseEntity<Void> {
        // Get restaurant ID
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        // Check if the submitter is the restaurant owner
        val isOwner = restaurantService.getRestaurant(restaurantIdentifier)?.ownerId == user.identifier

        if (!isOwner) {
            throw BaseForbiddenException()
        }

        // Put/remove restaurant meal's verification
        restaurantMealService.updateRestaurantMealVerification(restaurantIdentifier, mealId, verifyInput.verified)

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_MEAL_REPORT_PATH)
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

    @PutMapping(RESTAURANT_MEAL_PORTION_PATH)
    fun updateMealPortion(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody portion: PortionInput,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.updateUserPortion(user, restaurantIdentifier, mealId, portion)

        return ResponseEntity
                .ok()
                .build()
    }

    @DeleteMapping(RESTAURANT_MEAL_PORTION_PATH)
    fun deleteMealPortion(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteUserPortion(restaurantIdentifier, mealId, user)

        return ResponseEntity
                .ok()
                .build()
    }

    @DeleteMapping(RESTAURANT_MEAL_ID_PATH)
    fun deleteRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            user: User
    ): ResponseEntity<Void> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteRestaurantMeal(restaurantIdentifier, mealId, user)

        return ResponseEntity
                .ok()
                .build()
    }

    @PutMapping(RESTAURANT_MEAL_FAVORITE_PATH)
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
