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
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.DetailedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.SimplifiedMealContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toDetailedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toSimplifiedMealContainer
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.SubmissionService
import javax.validation.Valid

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class MealController(
        private val mealService: MealService,
        private val submissionService: SubmissionService
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
            submitter: Submitter?,
            @RequestParam count: Int?,
            @RequestParam skip: Int?,
            @RequestParam cuisines: Collection<String>?
    ): ResponseEntity<SimplifiedMealContainer> {

        // TODO - should filter by cuisines inside DB
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
                .body(toSimplifiedMealContainer(meals, submitter?.identifier))
    }

    @PostMapping(MEALS_SUGGESTED)
    fun createSuggestedMeal(
            @Valid @RequestBody meal: MealInput,
            user: User
    ): ResponseEntity<Void> {
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
            submitter: Submitter,
            count: Int?,
            skip: Int?
    ): ResponseEntity<List<Meal>> {

        val userCustomMeals = mealService
                .getUserCustomMeals(submitter.identifier, count, skip)
                //.map { CustomMealOutput() }
                .toList()

        return ResponseEntity.ok().body(userCustomMeals)
    }

    // TODO - has different repo method
    @PostMapping(MEALS_CUSTOM)
    fun createCustomMeal(
            @Valid @RequestBody meal: MealInput,
            submitter: Submitter
    ): ResponseEntity<Void> {
        //Due to validators we are sure fields are never null
        val createdMeal = mealService.createSuggestedMeal(
                name = meal.name!!,
                ingredients = meal.ingredients!!,
                cuisines = meal.cuisines!!,
                quantity = meal.quantity!!,
                submitterId = submitter.identifier
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
            submitter: Submitter,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody favorite: FavoriteInput
    ): ResponseEntity<Any> {
        mealService.setFavorite(mealId, submitter.identifier, favorite.isFavorite!!)
        return ResponseEntity.ok().build()
    }

    @GetMapping(MEAL)
    fun getMealInformation(
            submitter: Submitter?,
            @PathVariable(MEAL_ID_VALUE) mealId: Int
    ): ResponseEntity<DetailedMealOutput> {
        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        if (meal.isMealOwner(submitter)) {
            throw NotSubmissionOwnerException()
        }

        return ResponseEntity
                .ok()
                .body(toDetailedMealOutput(meal, submitter?.identifier))
    }

    @DeleteMapping(MEAL)
    fun deleteMeal(
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            submitter: Submitter
    ): ResponseEntity<Void> {
        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        if (meal.isMealOwner(submitter)) {
            throw NotSubmissionOwnerException()
        }

        submissionService.deleteSubmission(meal.identifier, submitter.identifier)

        return ResponseEntity
                .ok()
                .build()
    }
}