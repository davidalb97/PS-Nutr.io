package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.MEAL
import pt.isel.ps.g06.httpserver.common.MEALS
import pt.isel.ps.g06.httpserver.common.MEAL_ID_VALUE
import pt.isel.ps.g06.httpserver.common.MEAL_VOTE
import pt.isel.ps.g06.httpserver.common.exception.MealNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.output.SimplifiedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.output.toSimplifiedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.input.MealInput
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.RestaurantService
import javax.validation.Valid

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class MealController(private val mealService: MealService, private val restaurantService: RestaurantService) {

    @GetMapping(MEALS, consumes = [MediaType.ALL_VALUE])
    fun searchMeals(
            @RequestParam name: String,
            @RequestParam cuisines: Collection<String>?,
            @RequestParam apiType: String?
    ): ResponseEntity<Collection<SimplifiedMealOutput>> {
        val meals = mealService.searchMeals(name, cuisines, apiType)

        return ResponseEntity
                .ok()
                .body(meals.map { toSimplifiedMealOutput(it) })
    }

    @GetMapping(MEAL)
    fun getMealInformation(
            @PathVariable(MEAL_ID_VALUE) mealId: String,
            @RequestParam apiType: String?
    ): ResponseEntity<Meal> {
        val meal = mealService.getMeal(mealId, apiType)
        return meal?.let { ResponseEntity.ok().body(it) } ?: throw MealNotFoundException()
    }

    @PostMapping(MEALS)
    fun createMeal(@Valid meal: MealInput): ResponseEntity<Void> {
        //Due to validators we are sure fields are never null
        val createdMeal = mealService.createMeal(
                name = meal.name!!,
                ingredients = meal.ingredients!!,
                cuisines = meal.cuisines!!
        )

        if (meal.isRestaurantMeal()) {
            restaurantService.addRestaurantMeal(
                    mealId = createdMeal.identifier,
                    apiSubmitter = meal.restaurantApiSubmitterId!!,
                    restaurantApiId = meal.restaurantApiId,
                    submissionId = meal.submissionId
            )
        }

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(MEAL)
                        .buildAndExpand(createdMeal.identifier)
                        .toUri()
        ).build()
    }

    @DeleteMapping(MEAL)
    fun deleteMeal(@PathVariable(MEAL_ID_VALUE) mealId: String) = ""

    @PostMapping(MEAL_VOTE)
    fun addMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @PutMapping(MEAL_VOTE)
    fun updateMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @DeleteMapping(MEAL_VOTE)
    fun deleteMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestParam vote: String) = ""
}