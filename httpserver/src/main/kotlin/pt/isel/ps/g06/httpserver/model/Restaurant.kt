package pt.isel.ps.g06.httpserver.model

import java.net.URI

data class Restaurant(
        val identifier: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val image: URI?,
        val isFavorite: Boolean?,
        val userVote: Boolean?,
        val meals: Lazy<RestaurantMeal>,
        val suggestedMeals: Lazy<Meal>
)