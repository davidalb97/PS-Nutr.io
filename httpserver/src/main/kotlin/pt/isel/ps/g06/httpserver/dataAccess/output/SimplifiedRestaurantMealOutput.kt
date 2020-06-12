package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI

open class SimplifiedRestaurantMealOutput(
        val id: Int,
        val name: String,
        val votes: VotesOutput?,
        val isFavorite: Boolean?,
        val image: URI?
)

fun toSimplifiedRestaurantMealOutput(restaurantMeal: RestaurantMeal): SimplifiedRestaurantMealOutput {
    return SimplifiedRestaurantMealOutput(
            id = restaurantMeal.meal.identifier,
            name = restaurantMeal.meal.name,
            //TODO Proper votes
            votes = VotesOutput(VoteState.NOT_VOTED, 0, 0),
            isFavorite = restaurantMeal.meal.isFavorite,
            image = restaurantMeal.meal.image
    )
}