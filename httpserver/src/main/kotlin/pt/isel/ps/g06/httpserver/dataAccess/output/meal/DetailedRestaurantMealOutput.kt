package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.SimplifiedUserOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toSimplifiedUserOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toVotesOutput
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI
import java.time.OffsetDateTime

class DetailedRestaurantMealOutput(
        mealIdentifier: Int,
        name: String,
        imageUri: URI?,
        votes: VotesOutput?,
        isFavorite: Boolean,
        isSuggested: Boolean,
        val creationDate: OffsetDateTime?,
        val composedBy: MealCompositionOutput?,
        val nutritionalInfo: NutritionalInfoOutput,
        val portions: Collection<Int>,
        val createdBy: SimplifiedUserOutput?
) : SimplifiedRestaurantMealOutput(
        mealIdentifier = mealIdentifier,
        name = name,
        isFavorite = isFavorite,
        imageUri = imageUri,
        isSuggested = isSuggested,
        votes = votes
)

fun toDetailedRestaurantMealOutput(restaurantMeal: RestaurantMeal, userId: Int? = null): DetailedRestaurantMealOutput {
    val meal = restaurantMeal.meal
    val restaurant = restaurantMeal.restaurant

    val restaurantMealInfo = meal.getMealRestaurantInfo(restaurant.identifier.value)

    val votes = restaurantMealInfo?.let {
        toVotesOutput(
                it.votes.value,
                userId?.let { id -> it.userVote(id) } ?: VoteState.NOT_VOTED
        )
    }

    return DetailedRestaurantMealOutput(
            mealIdentifier = meal.identifier,
            name = meal.name,
            imageUri = meal.imageUri,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalValues),
            createdBy = meal.submitterInfo.value?.let { toSimplifiedUserOutput(it) },
            votes = votes,
            isSuggested = !meal.isUserMeal(),
            portions = restaurantMealInfo
                    ?.portions
                    ?.map { portion -> portion.amount }
                    ?.toList()
                    ?: emptyList()
    )
}