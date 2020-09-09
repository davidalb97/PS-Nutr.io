package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.MAX_COUNT
import pt.isel.ps.g06.httpserver.common.USER_FAVORITE_MEALS_PATH
import pt.isel.ps.g06.httpserver.common.USER_FAVORITE_RESTAURANTS_PATH
import pt.isel.ps.g06.httpserver.common.USER_FAVORITE_RESTAURANT_MEALS_PATH
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.SimplifiedMealContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toSimplifiedMealContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.SimplifiedRestaurantContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurant.toSimplifiedRestaurantContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal.FavoriteRestaurantMealsContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal.toFavoriteRestaurantMealsContainerOutput
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.service.MealService
import pt.isel.ps.g06.httpserver.service.RestaurantMealService
import pt.isel.ps.g06.httpserver.service.RestaurantService
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.ALL_VALUE]
)
class FavoriteControllerController(
        private val mealService: MealService,
        private val restaurantMealService: RestaurantMealService,
        private val restaurantService: RestaurantService
) {
    @GetMapping(USER_FAVORITE_MEALS_PATH)
    fun getFavoriteMealsFromUser(
            user: User,
            @RequestParam @Min(0) skip: Int?,
            @RequestParam @Min(0) @Max(MAX_COUNT) count: Int?
    ): ResponseEntity<SimplifiedMealContainer> {


        val userFavoriteMeals = mealService
                .getUserFavoriteMeals(user.identifier, count, skip)

        return ResponseEntity.ok()
                .body(toSimplifiedMealContainer(userFavoriteMeals, user.identifier))
    }

    @GetMapping(USER_FAVORITE_RESTAURANTS_PATH)
    fun getFavoriteRestaurantsFromUser(
            user: User,
            @RequestParam @Min(0) skip: Int?,
            @RequestParam @Min(0) @Max(MAX_COUNT) count: Int?
    ): ResponseEntity<SimplifiedRestaurantContainerOutput> {


        val favoriteRestaurants: Sequence<Restaurant> = restaurantService
                .getUserFavoriteRestaurants(user.identifier, count, skip)

        return ResponseEntity.ok()
                .body(toSimplifiedRestaurantContainerOutput(favoriteRestaurants.toList(), user.identifier))
    }

    @GetMapping(USER_FAVORITE_RESTAURANT_MEALS_PATH)
    fun getFavoriteRestaurantMealsFromUser(
            user: User,
            @RequestParam @Min(0) skip: Int?,
            @RequestParam @Min(0) @Max(MAX_COUNT) count: Int?
    ): ResponseEntity<FavoriteRestaurantMealsContainerOutput> {


        val favoriteRestaurantMeals: Sequence<RestaurantMeal> = restaurantMealService
                .getUserFavoriteMeals(user.identifier, count, skip)

        return ResponseEntity.ok()
                .body(toFavoriteRestaurantMealsContainerOutput(favoriteRestaurantMeals.toList(), user.identifier))
    }
}