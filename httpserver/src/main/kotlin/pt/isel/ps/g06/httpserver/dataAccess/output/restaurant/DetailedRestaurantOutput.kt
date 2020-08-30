package pt.isel.ps.g06.httpserver.dataAccess.output.restaurant

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.SimplifiedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toSimplifiedRestaurantMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.ISubmitterInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.SimplifiedUserOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toSimplifiedUserOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toVotesOutput
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.VoteState
import java.net.URI
import java.time.OffsetDateTime

class DetailedRestaurantOutput(
        identifier: String,
        name: String,
        image: URI?,
        latitude: Float,
        longitude: Float,
        favorites: FavoritesOutput,
        votes: VotesOutput,
        isReportable: Boolean,
        val cuisines: Collection<String>,
        @JsonSerialize(using = ToStringSerializer::class)
        val creationDate: OffsetDateTime?,
        val meals: Collection<SimplifiedRestaurantMealOutput>,
        val suggestedMeals: Collection<SimplifiedRestaurantMealOutput>,
        override val createdBy: SimplifiedUserOutput?
) : SimplifiedRestaurantOutput(
        identifier = identifier,
        name = name,
        latitude = latitude,
        longitude = longitude,
        favorites = favorites,
        votes = votes,
        isReportable = isReportable,
        image = image
), ISubmitterInfoOutput

fun toDetailedRestaurantOutput(restaurant: Restaurant, userId: Int? = null): DetailedRestaurantOutput {
    val mealToRestaurantMeal: (Meal) -> SimplifiedRestaurantMealOutput = { meal: Meal ->
        toSimplifiedRestaurantMealOutput(
                restaurantMeal = RestaurantMeal(
                        restaurant = restaurant,
                        meal = meal,
                        info = meal.getMealRestaurantInfo(restaurant.identifier.value)
                ),
                userId = userId
        )
    }
    return DetailedRestaurantOutput(
            identifier = restaurant.identifier.value.toString(),
            name = restaurant.name,
            image = restaurant.image,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            favorites = FavoritesOutput(
                    isFavorite = restaurant.isFavorite(userId),
                    isFavorable = restaurant.isFavorable(userId)
            ),
            votes = toVotesOutput(
                    isVotable = restaurant.isUserRestaurant(),
                    votes = restaurant.votes.value,
                    userVote = restaurant.userVote(userId)
            ),
            isReportable = restaurant.isReportable(userId),
            cuisines = restaurant.cuisines.map { it.name }.toList(),
            meals = restaurant.meals
                    .map { mealToRestaurantMeal(it) }
                    .toList(),
            suggestedMeals = restaurant.suggestedMeals
                    .map { mealToRestaurantMeal(it) }
                    .toList(),
            creationDate = restaurant.creationDate.value,
            createdBy = toSimplifiedUserOutput(restaurant.submitterInfo.value)
    )
}