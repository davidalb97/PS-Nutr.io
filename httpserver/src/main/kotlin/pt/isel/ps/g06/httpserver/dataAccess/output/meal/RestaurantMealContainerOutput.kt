package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import kotlin.streams.toList


data class RestaurantMealContainerOutput(
        val restaurantIdentifier: String,
        val suggestedMeals: Collection<SimplifiedRestaurantMealOutput>,
        val userMeals: Collection<SimplifiedRestaurantMealOutput>
)

fun toRestaurantMealContainerOutput(restaurant: Restaurant, userId: Int? = null): RestaurantMealContainerOutput {
    return RestaurantMealContainerOutput(
            restaurantIdentifier = restaurant.identifier.value.toString(),

            suggestedMeals = restaurant.suggestedMeals
                    .map { toSimplifiedRestaurantMealOutput(restaurant.identifier.value, it, userId) }
                    .toList(),

            userMeals = restaurant.meals
                    .map { toSimplifiedRestaurantMealOutput(restaurant.identifier.value, it, userId) }
                    .toList()
    )
}