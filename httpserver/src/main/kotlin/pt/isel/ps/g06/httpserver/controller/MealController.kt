package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidQueryParameter
import pt.isel.ps.g06.httpserver.common.exception.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.input.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.output.DetailedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.SimplifiedMealContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.toDetailedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toSimplifiedMealContainer
import pt.isel.ps.g06.httpserver.service.MealService
import javax.validation.Valid

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class MealController(private val mealService: MealService) {

    @GetMapping(MEAL)
    fun getMealInformation(@PathVariable(MEAL_ID_VALUE) mealId: Int): ResponseEntity<DetailedMealOutput> {
        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        return ResponseEntity
                .ok()
                .body(toDetailedMealOutput(meal))
    }

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
                .body(toSimplifiedMealContainer(meals))
    }

    @PostMapping(MEALS)
    fun createMeal(@Valid @RequestBody meal: MealInput): ResponseEntity<Void> {
        //TODO When there's authentication and users
        val submitter = 3

        //Due to validators we are sure fields are never null
        val createdMeal = mealService.createMeal(
                name = meal.name!!,
                ingredients = meal.ingredients!!,
                cuisines = meal.cuisines!!,
                quantity = meal.quantity!!,
                submitterId = submitter
        )

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(MEAL)
                        .buildAndExpand(createdMeal.identifier)
                        .toUri()
        ).build()
    }

    @DeleteMapping(MEAL)
    fun deleteMeal(@PathVariable(MEAL_ID_VALUE) mealId: String) = ""
}