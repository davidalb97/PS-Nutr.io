package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant

/**
 * Helper container for holding both [Meal] and [Restaurant]
 */
data class RestaurantMeal(
        val submissionId: Int?,
        val restaurant: Restaurant,
        val meal: Meal,
        val verified: Boolean
) {
    fun getRestaurantMealInfo(): MealRestaurantInfo? {
        return meal.getMealRestaurantInfo(restaurant.identifier.value)
    }
}