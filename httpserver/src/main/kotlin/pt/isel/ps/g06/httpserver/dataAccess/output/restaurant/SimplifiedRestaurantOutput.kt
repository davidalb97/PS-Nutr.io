package pt.isel.ps.g06.httpserver.dataAccess.output.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.output.vote.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toVotesOutput
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant

open class SimplifiedRestaurantOutput(
        val id: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val votes: VotesOutput?,
        val isFavorite: Boolean
)

fun toSimplifiedRestaurantOutput(restaurant: Restaurant, userId: Int? = null): SimplifiedRestaurantOutput {
    return SimplifiedRestaurantOutput(
            id = restaurant.identifier.value.toString(),
            name = restaurant.name,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            votes = toVotesOutput(
                    votes = restaurant.votes,
                    userVote = userId?.let { restaurant.userVote(userId) } ?: VoteState.NOT_VOTED
            ),
            isFavorite = userId?.let { restaurant.isFavorite(userId) } ?: false
    )
}