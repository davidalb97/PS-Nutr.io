package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.dataAccess.output.vote.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toVotesOutput
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI

open class SimplifiedRestaurantMealOutput(
        mealIdentifier: Int,
        name: String,
        isFavorite: Boolean,
        isSuggested: Boolean,
        imageUri: URI?,
        val votes: VotesOutput?
) : BaseMealOutput(
        mealIdentifier = mealIdentifier,
        name = name,
        isFavorite = isFavorite,
        isSuggested = isSuggested,
        imageUri = imageUri
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