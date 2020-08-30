package pt.isel.ps.g06.httpserver.model

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