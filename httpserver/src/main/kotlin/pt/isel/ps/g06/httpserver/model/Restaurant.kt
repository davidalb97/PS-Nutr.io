package pt.isel.ps.g06.httpserver.model

import java.net.URI

data class Restaurant(
        val identifier: RestaurantIdentifier,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val image: URI?,
        val isFavorite: Boolean?,
        val userVote: Boolean?,
        val meals: Sequence<RestaurantMeal>,
        val suggestedMeals: Sequence<Meal>,
        //TODO Add cuisines
        val cuisines: Sequence<String> = emptySequence()
)