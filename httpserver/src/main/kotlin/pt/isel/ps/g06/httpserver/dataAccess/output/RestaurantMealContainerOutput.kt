package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Restaurant

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