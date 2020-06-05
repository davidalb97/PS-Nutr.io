package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.MEAL
import pt.isel.ps.g06.httpserver.common.MEALS
import pt.isel.ps.g06.httpserver.common.MEAL_ID_VALUE
import pt.isel.ps.g06.httpserver.common.MEAL_VOTE
import pt.isel.ps.g06.httpserver.common.exception.MealNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.output.SimplifiedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.output.toSimplifiedMealOutput
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.service.MealService

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class MealController(private val mealService: MealService) {

    @GetMapping(MEAL)
    fun getMealInformation(
            @PathVariable(MEAL_ID_VALUE) mealId: String,
            @RequestParam apiType: String?
    ): ResponseEntity<Meal> {
        val meal = mealService.getMeal(mealId, apiType)
        return meal?.let { ResponseEntity.ok().body(it) } ?: throw MealNotFoundException()
    }

    @PostMapping(MEAL)
    fun postMeal(@PathVariable(MEAL_ID_VALUE) mealId: String) = ""

    @DeleteMapping(MEAL)
    fun deleteMeal(@PathVariable(MEAL_ID_VALUE) mealId: String) = ""

    @PostMapping(MEAL_VOTE)
    fun addMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @PutMapping(MEAL_VOTE)
    fun updateMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @DeleteMapping(MEAL_VOTE)
    fun deleteMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestParam vote: String) = ""
}