package pt.isel.ps.g06.httpserver.model

/**
 * Helper container for holding both [Meal] and [Restaurant]
 */
data class RestaurantMeal(
        val restaurant: Restaurant,
        val meal: Meal
)