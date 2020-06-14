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
import pt.isel.ps.g06.httpserver.dataAccess.input.MealInput
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.service.MealService
import javax.validation.Valid

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class MealController(private val mealService: MealService) {

    @GetMapping(MEAL)
    fun getMealInformation(@PathVariable(MEAL_ID_VALUE) mealId: Int): ResponseEntity<Meal> {
        return mealService.getMeal(mealId)
                ?.let { ResponseEntity.ok().body(it) }
                ?: throw MealNotFoundException()
    }

    @PostMapping(MEALS)
    fun createMeal(@Valid @RequestBody meal: MealInput): ResponseEntity<Void> {
        //TODO When there's authentication and users
        val submitter = 1

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

    @PostMapping(MEAL_VOTE)
    fun addMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @PutMapping(MEAL_VOTE)
    fun updateMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @DeleteMapping(MEAL_VOTE)
    fun deleteMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestParam vote: String) = ""
}