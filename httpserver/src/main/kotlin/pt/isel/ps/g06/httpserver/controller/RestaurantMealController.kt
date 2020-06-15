package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.clientError.ForbiddenInsertionResponseStatusException
import pt.isel.ps.g06.httpserver.common.exception.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.input.PortionInput
import pt.isel.ps.g06.httpserver.dataAccess.input.RestaurantMealInput
import pt.isel.ps.g06.httpserver.dataAccess.input.VoteInput
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
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val restaurantMealService: RestaurantMealService,
        private val restaurantIdentifierBuilder: RestaurantIdentifierBuilder
) {

    @GetMapping(RESTAURANT_MEALS, consumes = [MediaType.ALL_VALUE])
    fun getMealsForRestaurant(
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

        val restaurantMeal = restaurantMealService.getRestaurantMeal(restaurantIdentifier, mealId)

        return ResponseEntity
                .ok()
                .body(toDetailedRestaurantMealOutput(restaurantMeal))
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
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(id)

        val restaurant = restaurantService
                .getRestaurant(restaurantIdentifier)
                ?: throw RestaurantNotFoundException()

        val meal = mealService
                .getMeal(restaurantMeal.mealId!!)
                ?: throw MealNotFoundException()

        if (!meal.isUserMeal()) {
            throw ForbiddenInsertionResponseStatusException("Only meals created by you can be inserted!")
        }

        restaurantMealService.addRestaurantMeal(restaurantIdentifier, meal, userId)

        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromUriString(RESTAURANT_MEAL)
                        .buildAndExpand(restaurant.identifier.value.toString(), meal.identifier)
                        .toUri())
                .build()
    }


    @PostMapping(RESTAURANT_MEAL_PORTION)
    fun addMealPortion(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody portion: PortionInput
    ): ResponseEntity<Void> {
        //TODO When there's authentication
        val userId = 3
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.addRestaurantMealPortion(
                restaurantId = restaurantIdentifier,
                mealId = mealId,
                submitterId = userId,
                quantity = portion.quantity!!
        )

        return ResponseEntity.ok().build()
    }

    @PutMapping(RESTAURANT_MEAL_VOTE)
    fun alterRestaurantMealVote(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int,
            @Valid @RequestBody userVote: VoteInput
    ): ResponseEntity<Void> {
        //TODO When there's authentication
        val userId = 3
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.alterRestaurantMealVote(
                restaurantId = restaurantIdentifier,
                mealId = mealId,
                submitterId = userId,
                //Valid checks make sure user vote is not null
                vote = userVote.vote!!
        )

        return ResponseEntity.ok().build()
    }

    @DeleteMapping(RESTAURANT_MEAL_VOTE)
    fun deleteMealVote(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int
    ) {
        //TODO When there's authentication
        val userId = 3
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteRestaurantMealVote(restaurantIdentifier, mealId, userId)
    }

    @DeleteMapping(RESTAURANT_MEAL_PORTION)
    fun deleteMealPortion(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int
    ) {
        //TODO When there's authentication
        val userId = 3
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteUserPortion(restaurantIdentifier, mealId, userId)
    }

    @DeleteMapping(RESTAURANT_MEAL)
    fun deleteRestaurantMeal(
            @PathVariable(RESTAURANT_ID_VALUE) restaurantId: String,
            @PathVariable(MEAL_ID_VALUE) mealId: Int
    ): ResponseEntity<Void> {
        //TODO When there's authentication
        val userId = 3
        val restaurantIdentifier = restaurantIdentifierBuilder.extractIdentifiers(restaurantId)

        restaurantMealService.deleteRestaurantMeal(restaurantIdentifier, mealId, userId)

        return ResponseEntity.ok().build()
    }
}
