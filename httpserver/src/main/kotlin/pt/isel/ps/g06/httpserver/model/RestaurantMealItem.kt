package pt.isel.ps.g06.httpserver.model

open class RestaurantMealItem(
        val restaurantMealId: Int,
        mealId: Int,
        name: String,
        isFavorite: Boolean,
        image: String?,
        val userVote: Boolean?,
        val votes: Votes
): MealItem(
        identifier = mealId,
        name = name,
        isFavorite = isFavorite,
        image = image
)