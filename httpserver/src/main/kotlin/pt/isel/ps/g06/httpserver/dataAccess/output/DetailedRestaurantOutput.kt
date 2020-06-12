package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.VoteState

class DetailedRestaurantOutput(
        id: String,
        name: String,
        latitude: Float,
        longitude: Float,
        votes: VotesOutput?,
        isFavorite: Boolean?,
        val cuisines: Collection<String>,
//        val creationDate: OffsetDateTime, TODO
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

fun toDetailedRestaurantOutput(restaurant: Restaurant): DetailedRestaurantOutput {
    return DetailedRestaurantOutput(
            id = restaurant.identifier.toString(),
            name = restaurant.name,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            //TODO Proper votes
            votes = VotesOutput(VoteState.NOT_VOTED, 0, 0),
            isFavorite = restaurant.isFavorite,
            cuisines = restaurant.cuisines.toList(),
            meals = restaurant.meals.toList().map { toSimplifiedRestaurantMealOutput(it) },
            //TODO Proper mapping
            suggestedMeals = emptyList()
    )
}