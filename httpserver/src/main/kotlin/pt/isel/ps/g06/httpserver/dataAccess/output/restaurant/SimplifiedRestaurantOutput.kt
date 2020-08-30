package pt.isel.ps.g06.httpserver.dataAccess.output.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.output.modular.*
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toVotesOutput
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI

open class SimplifiedRestaurantOutput(
        identifier: String,
        name: String,
        image: URI?,
        favorites: FavoritesOutput,
        val latitude: Float,
        val longitude: Float,
        override val votes: VotesOutput,
        override val isReportable: Boolean
) : BasePublicSubmissionOutput<String>(
        identifier = identifier,
        name = name,
        image = image,
        favorites = favorites
), IVotableOutput, IReportableOutput, IFavorableOutput

fun toSimplifiedRestaurantOutput(restaurant: Restaurant, userId: Int? = null): SimplifiedRestaurantOutput {
    return SimplifiedRestaurantOutput(
            identifier = restaurant.identifier.value.toString(),
            name = restaurant.name,
            image = restaurant.image,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            votes = toVotesOutput(
                    isVotable = restaurant.isVotable(userId),
                    votes = restaurant.votes.value,
                    userVote = restaurant.userVote(userId)
            ),
            favorites = FavoritesOutput(
                    isFavorite = restaurant.isFavorite(userId),
                    isFavorable = restaurant.isFavorable(userId)
            ),
            isReportable = restaurant.isReportable(userId)
    )
}