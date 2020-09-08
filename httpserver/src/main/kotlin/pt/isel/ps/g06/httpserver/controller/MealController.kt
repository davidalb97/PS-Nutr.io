package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.input.meal.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.input.userActions.FavoriteInput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.DetailedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.SimplifiedMealContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toDetailedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toSimplifiedMealContainer
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.CannotDeletePublicSubmissionException
import pt.isel.ps.g06.httpserver.exception.problemJson.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.exception.problemJson.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.RestaurantMealService
import pt.isel.ps.g06.httpserver.service.SubmissionService
import pt.isel.ps.g06.httpserver.service.UserService
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class MealController(
        private val mealService: MealService,
        private val restaurantMealService: RestaurantMealService,
        private val submissionService: SubmissionService,
        private val userService: UserService
) {
    /**
     * Obtains all meals present in the database, filtered down by query parameters
     *
     * Defaults to [suggested]
     *
     * @param cuisines filters obtained meals by specific cuisine(s).
     * An empty collection will not filter any meal.
     */
    @GetMapping(MEALS_PATH)
    fun getMeals(
            user: User?,
            @RequestParam cuisines: Collection<String>?,
            @RequestParam @Min(0) skip: Int?,
            @RequestParam @Min(0) @Max(MAX_COUNT) count: Int?
    ): ResponseEntity<SimplifiedMealContainer> {

        var meals = mealService.getSuggestedMeals(
                skip = skip,
                count = count,
                cuisines = cuisines
        )

        //TODO cuisine filtering should happen in database as it breaks count param
        if (cuisines != null && cuisines.isNotEmpty()) {
            //Filter by user cuisines
            meals = meals.filter {
                it.cuisines.any { mealCuisines ->
                    cuisines.any { cuisine -> mealCuisines.name.equals(cuisine, ignoreCase = true) }
                }
            }
        }

        return ResponseEntity
                .ok()
                .body(toSimplifiedMealContainer(meals, user?.identifier))
    }

    @PostMapping(MEALS_PATH)
    fun createSuggestedMeal(
            @Valid @RequestBody meal: MealInput,
            user: User
    ): ResponseEntity<Void> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        //Due to validators we are sure fields are never null
        val createdMeal = mealService.createMeal(
                name = meal.name!!,
                ingredients = meal.ingredients!!,
                cuisines = meal.cuisines!!,
                quantity = meal.quantity!!,
                submitterId = user.identifier,
                mealType = MealType.SUGGESTED_MEAL
        )

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(MEAL_ID_PATH)
                        .buildAndExpand(createdMeal.identifier)
                        .toUri()
        ).build()
    }

    @PutMapping(MEAL_ID_FAVORITE_PATH)
    fun setFavoriteMeal(
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody favorite: FavoriteInput,
            user: User
    ): ResponseEntity<Any> {
        mealService.setFavorite(mealId, user.identifier, favorite.isFavorite!!)
        return ResponseEntity.ok().build()
    }

    @GetMapping(MEAL_ID_PATH)
    fun getMealInformation(
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            user: User?
    ): ResponseEntity<DetailedMealOutput> {
        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        if (meal.isMealOwner(user)) {
            throw NotSubmissionOwnerException()
        }

        return ResponseEntity
                .ok()
                .body(toDetailedMealOutput(meal, user?.identifier))
    }

    @DeleteMapping(MEAL_ID_PATH)
    fun deleteMeal(
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            user: User
    ): ResponseEntity<Void> {
        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        if (user.userRole != MOD_USER) {
            if(!meal.isMealOwner(user)) {
                throw NotSubmissionOwnerException()
            }
            //TODO replace with meal.isDeletable (send to output also to avoid this wrong request in the first place)
            val restaurantMeals = restaurantMealService.getRestaurantMealsByMealId(mealId)
            if(restaurantMeals.toList().isNotEmpty()) {
                throw CannotDeletePublicSubmissionException()
            }
        }

        submissionService.deleteSubmission(meal.identifier, user)

        return ResponseEntity
                .ok()
                .build()
    }
}

