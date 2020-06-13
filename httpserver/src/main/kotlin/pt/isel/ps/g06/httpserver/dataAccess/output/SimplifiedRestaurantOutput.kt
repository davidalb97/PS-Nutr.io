package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.VoteState

open class SimplifiedRestaurantOutput(
        val id: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val votes: VotesOutput?,
        val isFavorite: Boolean?
)

fun toSimplifiedRestaurantOutput(restaurant: Restaurant, userId: Int?): SimplifiedRestaurantOutput {
    return SimplifiedRestaurantOutput(
            id = restaurant.identifier.toString(),
            name = restaurant.name,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            votes = VotesOutput(
                    userVote = userId?.let { restaurant.userVote(userId) } ?: VoteState.NOT_VOTED,
                    positive = restaurant.votes.positive,
                    negative = restaurant.votes.positive
            ),
            isFavorite = userId?.let { restaurant.isFavorite(userId) } ?: false
    )
}