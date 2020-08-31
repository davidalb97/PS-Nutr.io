package pt.isel.ps.g06.httpserver.model

/**
 * Helper container for holding both [Meal] and [Restaurant]
 */
class RestaurantMeal(
        val restaurant: Restaurant,
        val meal: Meal,
        val info: MealRestaurantInfo?
) {

    fun getRestaurantMealInfo(): MealRestaurantInfo? {
        return meal.getMealRestaurantInfo(restaurant.identifier.value)
    }
}