package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI

//TODO Better hierarchy because Simplified Meal output is equal (but without votes)
open class SimplifiedRestaurantMealOutput(
        val mealIdentifier: Int,
        val name: String,
        val votes: VotesOutput?,
        val isFavorite: Boolean,
        val isSuggested: Boolean,
        val imageUri: URI?
)


fun toSimplifiedRestaurantMealOutput(
        restaurantIdentifier: RestaurantIdentifier,
        meal: Meal,
        userId: Int? = null
): SimplifiedRestaurantMealOutput {
    val votes = meal.restaurantInfoSupplier(restaurantIdentifier)?.let {
        toVotesOutput(
                it.votes.value,
                userId?.let { id -> it.userVote(id) } ?: VoteState.NOT_VOTED
        )
    }

    return SimplifiedRestaurantMealOutput(
            mealIdentifier = meal.identifier,
            name = meal.name,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            imageUri = meal.imageUri,
            votes = votes,
            isSuggested = !meal.isUserMeal()
    )
}