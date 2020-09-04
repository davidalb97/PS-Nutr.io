package pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal

import pt.isel.ps.g06.httpserver.model.RestaurantMeal

data class FavoriteRestaurantMealsContainerOutput(
        val meals: Collection<SimplifiedFavoriteRestaurantMealOutput>
)

fun toFavoriteRestaurantMealsContainerOutput(
        meals: Collection<RestaurantMeal>,
        userId: Int? = null
): FavoriteRestaurantMealsContainerOutput = FavoriteRestaurantMealsContainerOutput(
        meals = meals.map {
            toSimplifiedFavoriteRestaurantMealOutput(
                    restaurantMeal = it,
                    userId = userId
            )
        }
)