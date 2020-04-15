package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/meal")
class MealController {

    @GetMapping("/{mealId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMealInformation(@PathVariable mealId: String) = ""

    @PostMapping("/{mealId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun postMeal(@PathVariable mealId: String) = ""

    @DeleteMapping("/{mealId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteMeal(@PathVariable mealId: String) = ""

    @PostMapping("/{mealId}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addMealVote(@PathVariable mealId: String, @RequestBody vote: String) = ""

    @PutMapping("/{mealId}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateMealVote(@PathVariable mealId: String, @RequestBody vote: String) = ""

    @DeleteMapping("/{mealId}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteMealVote(@PathVariable mealId: String, vote: String) = ""
}