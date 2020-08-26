package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.input.FavoriteInput
import pt.isel.ps.g06.httpserver.dataAccess.input.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.*
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.SubmissionService
import pt.isel.ps.g06.httpserver.service.UserService
import javax.validation.Valid

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class MealController(
        private val mealService: MealService,
        private val submissionService: SubmissionService,
        private val userService: UserService
) {
    /**
     * Obtains all meals present in the database, filtered down by query parameters
     *
     * @param mealTypes filters by meal type. View [allowedMealTypes] to see possible values;
     * Defaults to [suggested]
     *
     * @param cuisines filters obtained meals by specific cuisine(s).
     * An empty collection will not filter any meal.
     */
    @GetMapping(MEALS_SUGGESTED)
    fun getMeals(
            user: User?,
            @RequestParam count: Int?,
            @RequestParam skip: Int?,
            @RequestParam cuisines: Collection<String>?
    ): ResponseEntity<SimplifiedMealContainer> {

        var meals = mealService.getSuggestedMeals(
                count = count,
                skip = skip,
                cuisines = cuisines
        )

        if (cuisines != null && cuisines.isNotEmpty()) {
            //Filter by user cuisines
            meals = meals.filter {
                it.cuisines.any { mealCuisines ->
                    cuisines.any { cuisine -> mealCuisines.name.equals(cuisine, ignoreCase = true) }
                }
            }
        }

        //Perform final result reductions
        meals = meals.drop(skip ?: 0)
        if (count != null) {
            meals = meals.take(count)
        }

        return ResponseEntity
                .ok()
                .body(toSimplifiedMealContainer(meals, user?.identifier))
    }

    @PostMapping(MEALS_SUGGESTED)
    fun createSuggestedMeal(
            @Valid @RequestBody meal: MealInput,
            user: User
    ): ResponseEntity<Void> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        //Due to validators we are sure fields are never null
        val createdMeal = mealService.createSuggestedMeal(
                name = meal.name!!,
                ingredients = meal.ingredients!!,
                cuisines = meal.cuisines!!,
                quantity = meal.quantity!!,
                submitterId = user.identifier
        )

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(MEAL)
                        .buildAndExpand(createdMeal.identifier)
                        .toUri()
        ).build()
    }

    @GetMapping(MEALS_CUSTOM)
    fun getCustomMealsFromUser(
            user: User,
            @RequestParam count: Int?,
            @RequestParam skip: Int?
    ): ResponseEntity<List<Meal>> {

        val userCustomMeals = mealService
                .getUserCustomMeals(user.identifier, count, skip)
                .toList()

        return ResponseEntity.ok().body(userCustomMeals)
    }

    // TODO - has different repo method
    @PostMapping(MEALS_CUSTOM)
    fun createCustomMeal(
            @Valid @RequestBody meal: MealInput,
            user: User
    ): ResponseEntity<Void> {
        //Due to validators we are sure fields are never null
        val createdMeal = mealService.createCustomMeal(
                submitterId = user.identifier,
                name = meal.name!!,
                quantity = meal.quantity!!,
                ingredients = meal.ingredients!!,
                cuisines = meal.cuisines!!
        )

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(MEAL)
                        .buildAndExpand(createdMeal.identifier)
                        .toUri()
        ).build()
    }

    @PutMapping(MEAL_FAVORITE)
    fun setFavoriteMeal(
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody favorite: FavoriteInput,
            user: User
    ): ResponseEntity<Any> {
        mealService.setFavorite(mealId, user.identifier, favorite.isFavorite!!)
        return ResponseEntity.ok().build()
    }

    @GetMapping(MEAL)
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

    @DeleteMapping(MEAL)
    fun deleteMeal(
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            user: User
    ): ResponseEntity<Void> {
        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        if (meal.isMealOwner(user)) {
            throw NotSubmissionOwnerException()
        }

        submissionService.deleteSubmission(meal.identifier, user.identifier, false)

        return ResponseEntity
                .ok()
                .build()
    }
}