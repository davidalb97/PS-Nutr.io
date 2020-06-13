package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.VoteState
import java.time.OffsetDateTime

class DetailedRestaurantOutput(
        id: String,
        name: String,
        latitude: Float,
        longitude: Float,
        votes: VotesOutput?,
        isFavorite: Boolean?,
        val cuisines: Collection<String>,
        val creationDate: OffsetDateTime?,
        val meals: Collection<SimplifiedRestaurantMealOutput>,
        val suggestedMeals: Collection<SimplifiedRestaurantMealOutput>
) : SimplifiedRestaurantOutput(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        votes = votes,
        isFavorite = isFavorite
)

fun toDetailedRestaurantOutput(restaurant: Restaurant, userId: Int?): DetailedRestaurantOutput {
    return DetailedRestaurantOutput(
            id = restaurant.identifier.toString(),
            name = restaurant.name,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            votes = VotesOutput(
                    userVote = userId?.let { restaurant.userVote(userId) } ?: VoteState.NOT_VOTED,
                    positive = restaurant.votes.positive,
                    negative = restaurant.votes.positive
            ),
            isFavorite = userId?.let { restaurant.isFavorite(userId) } ?: false,
            cuisines = restaurant.cuisines.map { it.name }.toList(),
            meals = restaurant.meals.toList().map { toSimplifiedRestaurantMealOutput(it, userId) },
            suggestedMeals = restaurant.suggestedMeals.toList().map { toSimplifiedMealOutput(it, userId) },
            creationDate = restaurant.creationDate.value
    )
}