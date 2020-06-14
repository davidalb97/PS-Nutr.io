package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.MEAL
import pt.isel.ps.g06.httpserver.common.MEALS
import pt.isel.ps.g06.httpserver.common.MEAL_ID_VALUE
import pt.isel.ps.g06.httpserver.common.MEAL_VOTE
import pt.isel.ps.g06.httpserver.common.exception.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.input.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.output.DetailedMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toDetailedMealOutput
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

    @PostMapping(MEAL_VOTE)
    fun addMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @PutMapping(MEAL_VOTE)
    fun updateMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestBody vote: String) = ""

    @DeleteMapping(MEAL_VOTE)
    fun deleteMealVote(@PathVariable(MEAL_ID_VALUE) mealId: String, @RequestParam vote: String) = ""
}