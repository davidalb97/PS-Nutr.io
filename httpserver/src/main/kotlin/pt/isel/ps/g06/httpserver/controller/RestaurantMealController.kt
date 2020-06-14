package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.ForbiddenInsertionException
import pt.isel.ps.g06.httpserver.common.exception.MealNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.input.PortionInput
import pt.isel.ps.g06.httpserver.dataAccess.input.RestaurantMealInput
import pt.isel.ps.g06.httpserver.dataAccess.output.DetailedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.RestaurantMealContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toDetailedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toRestaurantMealContainerOutput
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.RestaurantMealService
import pt.isel.ps.g06.httpserver.service.RestaurantService
import javax.validation.Valid

@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
class RestaurantMealController(
        private val mealService: MealService,
        private val restaurantMealService: RestaurantMealService,
        private val restaurantService: RestaurantService,
        private val restaurantIdentifierBuilder: RestaurantIdentifierBuilder
) {

    @GetMapping(RESTAURANT_MEALS, consumes = [MediaType.ALL_VALUE])
    fun getMealsFromRestaurant(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String
    ): ResponseEntity<RestaurantMealContainerOutput> {
        val (submitterId, submissionId, apiId) = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        val restaurant = restaurantService
                .getRestaurant(submitterId, submissionId, apiId)
                ?: throw RestaurantNotFoundException()

        return ResponseEntity
                .ok()
                .body(toRestaurantMealContainerOutput(restaurant))
    }

    @GetMapping(RESTAURANT_MEAL, consumes = [MediaType.ALL_VALUE])
    fun getRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int
    ): ResponseEntity<DetailedRestaurantMealOutput> {
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        //TODO If Restaurant Info is not present, throw not found (for user meals)
        //TODO Check owner (if any) and then check if restaurant info is present
        return ResponseEntity
                .ok()
                .body(toDetailedRestaurantMealOutput(restaurantIdentifier, meal))
    }


    /**
     *  Adds an existing User created meal to an existing Restaurant, allowing other
     *  users to see it, add portions, vote and report it.
     *
     *  Internally and as a side effect, if a Restaurant exists
     *  but is not present in the database, it is first inserted into the database.
     *
     *  This logic does not change the client's behavior on accessing that restaurant.
     */
    @PostMapping(RESTAURANT_MEALS, consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) id: String,
            @Valid @RequestBody restaurantMeal: RestaurantMealInput
    ): ResponseEntity<Void> {
        val userId = 3  //TODO For when there's authentication
        val (submitterId, submissionId, apiId) = restaurantIdentifierBuilder.extractIdentifiers(id)

        val restaurant = restaurantService.getRestaurant(
                submitterId = submitterId,
                submissionId = submissionId,
                apiId = apiId
        ) ?: throw RestaurantNotFoundException()

        val meal = mealService.getMeal(restaurantMeal.mealId!!) ?: throw MealNotFoundException()

        if (!meal.isUserMeal()) {
            throw ForbiddenInsertionException("Only meals created by you can be inserted!")
        }

        val restaurantMealId = restaurantMealService.addRestaurantMeal(restaurant, meal, userId)

        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromUriString(RESTAURANT_MEAL)
                        .buildAndExpand(restaurantMealId)
                        .toUri())
                .build()
    }


    @PostMapping(RESTAURANT_MEAL_PORTION)
    fun addMealPortion(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody portion: PortionInput
    ): ResponseEntity<Void> {
//        //TODO When there's authentication
        val submitterId = 3
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        val restaurant = restaurantService
                .getRestaurant(restaurantIdentifier.submitterId, restaurantIdentifier.submissionId, restaurantIdentifier.apiId)
                ?: throw RestaurantNotFoundException()

        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        restaurantMealService.addRestaurantMealPortion(
                restaurant = restaurant,
                meal = meal,
                submitterId = submitterId,
                quantity = portion.quantity!!
        )

        return ResponseEntity.ok().build()
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
