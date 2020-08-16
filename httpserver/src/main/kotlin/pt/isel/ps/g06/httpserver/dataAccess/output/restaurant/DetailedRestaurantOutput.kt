package pt.isel.ps.g06.httpserver.dataAccess.output.restaurant

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.SimplifiedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toSimplifiedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toVotesOutput
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import java.time.OffsetDateTime

class DetailedRestaurantOutput(
        id: String,
        name: String,
        latitude: Float,
        longitude: Float,
        votes: VotesOutput?,
        isFavorite: Boolean,
        val cuisines: Collection<String>,
        @JsonSerialize(using = ToStringSerializer::class)
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

fun toDetailedRestaurantOutput(restaurant: Restaurant, userId: Int? = null): DetailedRestaurantOutput {
    return DetailedRestaurantOutput(
            id = restaurant.identifier.value.toString(),
            name = restaurant.name,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            votes = toVotesOutput(
                    votes = restaurant.votes,
                    userVote = userId?.let { restaurant.userVote(userId) } ?: VoteState.NOT_VOTED
            ),
            isFavorite = userId?.let { restaurant.isFavorite(userId) } ?: false,
            cuisines = restaurant.cuisines.map { it.name }.toList(),

            meals = restaurant.meals
                    .map { toSimplifiedRestaurantMealOutput(restaurant.identifier.value, it, userId) }
                    .toList(),

            suggestedMeals = restaurant.suggestedMeals
                    .map { toSimplifiedRestaurantMealOutput(restaurant.identifier.value, it, userId) }
                    .toList(),

            creationDate = restaurant.creationDate.value
    )
}