package pt.isel.ps.g06.httpserver.dataAccess.output.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.output.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.*
import pt.isel.ps.g06.httpserver.dataAccess.output.toVotesOutput
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import java.net.URI

open class SimplifiedRestaurantOutput(
        identifier: String,
        name: String,
        image: URI?,
        val latitude: Float,
        val longitude: Float,
        override val favorites: FavoritesOutput,
        override val votes: VotesOutput,
        override val isReportable: Boolean
) : BasePublicSubmissionOutput<String>(
        identifier = identifier,
        name = name,
        image = image
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