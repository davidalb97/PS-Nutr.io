package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.VoteState

fun toSimplifiedMealOutput(meal: Meal, userId: Int?): SimplifiedRestaurantMealOutput {
    return SimplifiedRestaurantMealOutput(
            id = meal.identifier,
            name = meal.name,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            image = meal.image,
            votes = VotesOutput(
                    VoteState.NOT_VOTED,
                    0,
                    0
            )
    )
}