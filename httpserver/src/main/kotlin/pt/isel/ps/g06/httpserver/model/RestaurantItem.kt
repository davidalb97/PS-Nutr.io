package pt.isel.ps.g06.httpserver.model

open class RestaurantItem(
        val identifier: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val image: String?,
        val votes: Votes,
        val userVote: Boolean?,
        val isFavorite: Boolean
)