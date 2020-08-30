package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toVotesOutput
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.NutritionalValues
import pt.isel.ps.g06.httpserver.model.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI

open class SimplifiedRestaurantMealOutput(
        mealIdentifier: Int,
        name: String,
        isFavorite: Boolean,
        isSuggested: Boolean,
        isVerified: Boolean,
        imageUri: URI?,
        nutritionalInfo: NutritionalInfoOutput,
        val votes: VotesOutput?
) : BaseMealOutput(
        mealIdentifier = mealIdentifier,
        name = name,
        isFavorite = isFavorite,
        isSuggested = isSuggested,
        isVerified = isVerified,
        //A restaurant meal is always votable
        isVotable = true,
        imageUri = imageUri,
        nutritionalInfo = nutritionalInfo
)


fun toSimplifiedRestaurantMealOutput(
        restaurantIdentifier: RestaurantIdentifier,
        meal: Meal,
        userId: Int? = null
): SimplifiedRestaurantMealOutput {
    val votes = meal.getMealRestaurantInfo(restaurantIdentifier)?.let {
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
            isSuggested = !meal.isUserMeal(),
            isVerified = false, // TODO
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalValues)
    )
}