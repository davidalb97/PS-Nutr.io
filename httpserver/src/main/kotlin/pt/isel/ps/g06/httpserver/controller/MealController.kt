package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidQueryParameter
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.input.FavoriteInput
import pt.isel.ps.g06.httpserver.dataAccess.input.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.*
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.SubmissionService
import pt.isel.ps.g06.httpserver.service.UserService
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
    @GetMapping(MEALS)
    fun getMeals(
            submitter: Submitter?,
            @RequestParam mealTypes: Collection<String>?,
            @RequestParam skip: Int?,
            @RequestParam count: Int?,
            @RequestParam cuisines: Collection<String>?
    ): ResponseEntity<SimplifiedMealContainer> {
        val types = if (mealTypes == null || mealTypes.isEmpty()) listOf(suggested)
        else mealTypes

        if (types.any { !allowedMealTypes.contains(it) }) {
            //Make sure all type filters are allowed
            throw InvalidQueryParameter("Invalid query parameter! Allowed ones are: $allowedMealTypes")
        }

        var meals = mealService.getSuggestedMeals()

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

    @GetMapping(USER_MEAL)
    fun getCustomMealsFromUser(submitter: Submitter?): ResponseEntity<List<Meal>> {
        submitter ?: throw NotAuthenticatedException()

        val userCustomMeals = mealService
                .getUserCustomMeals(submitter.identifier)
                //.map { CustomMealOutput() }
                .toList()

        return ResponseEntity.ok().body(userCustomMeals)
    }

    @PutMapping(MEAL_FAVORITE)
    fun setFavoriteMeal(
            submitter: Submitter?,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody favorite: FavoriteInput
    ): ResponseEntity<Any> {
        submitter ?: throw NotAuthenticatedException()

        mealService.setFavorite(mealId, submitter.identifier, favorite.isFavorite!!)
        return ResponseEntity.ok().build()
    }

    @GetMapping(MEAL)
    fun getMealInformation(
            submitter: Submitter?,
            @PathVariable(MEAL_ID_VALUE) mealId: Int
    ): ResponseEntity<DetailedMealOutput> {
        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        return ResponseEntity
                .ok()
                .body(toDetailedMealOutput(meal, submitter?.identifier))
    }

    @PostMapping(MEALS)
    fun createMeal(
            @Valid @RequestBody meal: MealInput,
            submitter: Submitter?
    ): ResponseEntity<Void> {
        submitter ?: throw NotAuthenticatedException()

        //Due to validators we are sure fields are never null
        val createdMeal = mealService.createMeal(
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

    @DeleteMapping(MEAL)
    fun deleteMeal(
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            submitter: Submitter?
    ): ResponseEntity<Void> {
        submitter ?: throw NotAuthenticatedException()

        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        if (!meal.isUserMeal() || meal.submitterInfo.value!!.identifier != submitter.identifier) {
            throw NotSubmissionOwnerException()
        }

        submissionService.deleteSubmission(meal.identifier, submitter.identifier)

        return ResponseEntity
                .ok()
                .build()
    }
}