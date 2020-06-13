package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI

open class SimplifiedRestaurantMealOutput(
        val id: Int,
        val name: String,
        val votes: VotesOutput?,
        val isFavorite: Boolean,
        val image: URI?
)

fun toSimplifiedRestaurantMealOutput(restaurantMeal: RestaurantMeal, userId: Int?): SimplifiedRestaurantMealOutput {
    return SimplifiedRestaurantMealOutput(
            id = restaurantMeal.meal.identifier,
            name = restaurantMeal.meal.name,
            votes = VotesOutput(
                    userVote = userId?.let { restaurantMeal.userVote(userId) } ?: VoteState.NOT_VOTED,
                    positive = restaurantMeal.votes.positive,
                    negative = restaurantMeal.votes.positive
            ),
            isFavorite = userId?.let { restaurantMeal.meal.isFavorite(userId) } ?: false,
            image = restaurantMeal.meal.image
    )
}