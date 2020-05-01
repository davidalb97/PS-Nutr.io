package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.MEAL
import pt.isel.ps.g06.httpserver.common.MEAL_ID_VALUE
import pt.isel.ps.g06.httpserver.common.MEAL_VOTE

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class MealController {

    @GetMapping(MEAL)
    fun getMealInformation(@PathVariable(MEAL_ID_VALUE) mealId: String) = ""

    @PostMapping(MEAL)
    fun postMeal(@PathVariable(MEAL_ID_VALUE) mealId: String) = ""

    @DeleteMapping(MEAL)
    fun deleteMeal(@PathVariable(MEAL_ID_VALUE) mealId: String) = ""

    @PostMapping(MEAL_VOTE)
    fun addMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @PutMapping(MEAL_VOTE)
    fun updateMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @DeleteMapping(MEAL_VOTE)
    fun deleteMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, vote: String) = ""
}