package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.input.meal.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.SimplifiedMealContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toSimplifiedMealContainer
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.MealService
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.ALL_VALUE]
)
class CustomMealController(private val mealService: MealService) {

    @GetMapping(USER_CUSTOM_MEALS_PATH)
    fun getCustomMealsFromUser(
            user: User,
            @RequestParam @Min(0) skip: Int?,
            @RequestParam @Min(0) @Max(MAX_COUNT) count: Int?
    ): ResponseEntity<SimplifiedMealContainer> {

        val userCustomMeals = mealService
                .getUserCustomMeals(user.identifier, skip, count)

        return ResponseEntity.ok().body(toSimplifiedMealContainer(userCustomMeals, user.identifier))
    }

    // TODO - has different repo method
    @PostMapping(USER_CUSTOM_MEALS_PATH)
    fun createCustomMeal(
            @Valid @RequestBody meal: MealInput,
            user: User
    ): ResponseEntity<Void> {
        //Due to validators we are sure fields are never null
        val createdMeal = mealService.createMeal(
                submitterId = user.identifier,
                name = meal.name!!,
                quantity = meal.quantity!!,
                ingredients = meal.ingredients!!,
                cuisines = meal.cuisines!!,
                mealType = MealType.CUSTOM
        )

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(MEAL_ID_PATH)
                        .buildAndExpand(createdMeal.identifier)
                        .toUri()
        ).build()
    }

    @PutMapping(USER_CUSTOM_MEAL_ID_PATH)
    fun editCustomMeal(
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody meal: MealInput,
            user: User
    ): ResponseEntity<Void> {
        //Due to validators we are sure fields are never null
        val updatedMeal = mealService.editMeal(
                submissionId = mealId,
                submitterId = user.identifier,
                name = meal.name!!,
                quantity = meal.quantity!!,
                ingredients = meal.ingredients!!,
                cuisines = meal.cuisines!!,
                mealType = MealType.CUSTOM
        )

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(MEAL_ID_PATH)
                        .buildAndExpand(updatedMeal.identifier)
                        .toUri()
        ).build()
    }
}