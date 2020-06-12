package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine
import java.net.URI
import java.time.OffsetDateTime

data class Restaurant(
        val identifier: Lazy<String>,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val image: URI?,
        val isFavorite: (Int) -> Boolean,
        //TODO Use vote state!
        val userVote: (Int) -> Boolean?,
        val votes: Votes?,
        val creatorInfo: Lazy<Creator?>,
        val creationDate: Lazy<OffsetDateTime?>,
        val meals: Sequence<RestaurantMeal>,
        val suggestedMeals: Sequence<Meal>,
        val cuisines: Sequence<Cuisine>
)