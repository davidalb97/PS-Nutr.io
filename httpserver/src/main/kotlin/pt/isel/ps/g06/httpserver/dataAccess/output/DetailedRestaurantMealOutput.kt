package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI
import java.time.OffsetDateTime

class DetailedRestaurantMealOutput(
        id: Int,
        name: String,
        imageUri: URI?,
        votes: VotesOutput?,
        isFavorite: Boolean,
        val creationDate: OffsetDateTime?,
        val composedBy: MealCompositionOutput?,
        val nutritionalInfo: NutritionalInfoOutput,
        val portions: Collection<Int>,
        val createdBy: SimplifiedUserOutput?
) : SimplifiedRestaurantMealOutput(
        mealIdentifier = id,
        name = name,
        votes = votes,
        isFavorite = isFavorite,
        imageUri = imageUri
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
            id = meal.identifier,
            name = meal.name,
            imageUri = meal.imageUri,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalValues),
            createdBy = meal.creatorInfo.value?.let { toSimplifiedUserOutput(it) },
            votes = votes,
            portions = restaurantMealInfo
                    ?.portions
                    ?.map { portion -> portion.amount }
                    ?.toList()
                    ?: emptyList()
    )
}