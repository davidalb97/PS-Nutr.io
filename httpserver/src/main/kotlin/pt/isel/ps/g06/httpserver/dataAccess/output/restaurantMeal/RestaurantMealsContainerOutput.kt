package pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal

import pt.isel.ps.g06.httpserver.model.RestaurantMeal

data class RestaurantMealsContainerOutput(
        val meals: Collection<SimplifiedRestaurantMealOutput>
)

fun toRestaurantMealsContainerOutput(
        meals: Collection<RestaurantMeal>,
        userId: Int? = null
): RestaurantMealsContainerOutput = RestaurantMealsContainerOutput(
        meals = meals.map {
            toSimplifiedRestaurantMealOutput(
                    restaurantMeal = it,
                    userId = userId
            )
        }
)