package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI

open class SimplifiedRestaurantMealOutput(
        val mealIdentifier: Int,
        val name: String,
        val votes: VotesOutput?,
        val isFavorite: Boolean,
        val imageUri: URI?
)

//fun toSimplifiedRestaurantMealOutput(restaurantMeal: RestaurantMeal, userId: Int?): SimplifiedRestaurantMealOutput {
//    return SimplifiedRestaurantMealOutput(
//            id = restaurantMeal.meal.identifier,
//            name = restaurantMeal.meal.name,
//            votes = VotesOutput(
//                    userVote = userId?.let { restaurantMeal.userVote(userId) } ?: VoteState.NOT_VOTED,
//                    positive = restaurantMeal.votes.positive,
//                    negative = restaurantMeal.votes.positive
//            ),
//            isFavorite = userId?.let { restaurantMeal.meal.isFavorite(userId) } ?: false,
//            image = restaurantMeal.meal.image
//    )
//}

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
            votes = votes
    )
}