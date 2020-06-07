package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.dataAccess.model.SimplifiedMealOutputModel
import pt.isel.ps.g06.httpserver.dataAccess.model.toSimplifiedMeal
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.service.MealService

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class RestaurantMealsController(private val mealService: MealService) {

    @GetMapping(RESTAURANT_MEALS)
    fun getMealsFromRestaurant(@PathVariable(RESTAURANT_ID_VALUE) id: Int): Collection<SimplifiedMealOutputModel>? =
            mealService.getAllMealsFromRestaurant(id)?.map { toSimplifiedMeal(it) }


    @PostMapping(RESTAURANT_MEALS)
    fun addRestaurantMeal(@PathVariable(RESTAURANT_ID_VALUE) id: String, @RequestBody meal: String) {

    }

    @PostMapping(RESTAURANT_MEAL_PORTION)
    fun addMealPortion(@PathVariable(RESTAURANT_ID_VALUE) id: String, @PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody portion: String) {

    }

    @PostMapping(RESTAURANT_MEAL_REPORT)
    fun addMealReport(@PathVariable(RESTAURANT_ID_VALUE) id: String, @PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody report: String) {

    }

    @PostMapping(RESTAURANT_MEAL_VOTE)
    fun addMealVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) {

    }

    @PutMapping(RESTAURANT_MEAL_REPORT)
    fun updateMealReport(@PathVariable(RESTAURANT_ID_VALUE) id: String, @PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody portion: String) {

    }

    @PutMapping(RESTAURANT_MEAL_VOTE)
    fun updateMealVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) {

    }

    @DeleteMapping(RESTAURANT_MEAL_PORTION)
    fun deleteMealPortion(@PathVariable(RESTAURANT_ID_VALUE) id: String, @PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody portion: String) {

    }

    @DeleteMapping(RESTAURANT_MEAL_VOTE)
    fun deleteMealVote(@PathVariable(RESTAURANT_ID_VALUE) id: String, @PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) {

    }
}
