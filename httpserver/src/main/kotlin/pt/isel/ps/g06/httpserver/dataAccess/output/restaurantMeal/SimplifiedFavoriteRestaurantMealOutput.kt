package pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal

import pt.isel.ps.g06.httpserver.dataAccess.output.*
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.BaseMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.IReportableOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.IVotableOutput
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import java.net.URI

open class SimplifiedFavoriteRestaurantMealOutput(
        //This is the MEAL identifier!
        identifier: Int,
        val restaurantIdentifier: String,
        name: String,
        favorites: FavoritesOutput,
        image: URI?,
        nutritionalInfo: NutritionalInfoOutput,
        isVerified: Boolean,
        isSuggested: Boolean,
        isReportable: Boolean,
        votes: VotesOutput
) : SimplifiedRestaurantMealOutput(
        identifier = identifier,
        name = name,
        favorites = favorites,
        image = image,
        nutritionalInfo = nutritionalInfo,
        isVerified = isVerified,
        isSuggested = isSuggested,
        isReportable = isReportable,
        votes = votes
), IReportableOutput, IVotableOutput


fun toSimplifiedFavoriteRestaurantMealOutput(
        restaurantMeal: RestaurantMeal,
        userId: Int? = null
): SimplifiedFavoriteRestaurantMealOutput {
    val meal = restaurantMeal.meal
    val restaurantMealInfo = restaurantMeal.info
    val isMealOwner = meal.isUserMeal() && userId?.let { meal.submitterInfo.value?.identifier == userId } ?: false
    return SimplifiedFavoriteRestaurantMealOutput(
            identifier = meal.identifier,
            name = meal.name,
            image = meal.image,
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalInfo),
            restaurantIdentifier = restaurantMeal.restaurant.identifier.value.toString(),
            //All RestaurantMeal info is only available when inserted on the database
            favorites = FavoritesOutput(
                    isFavorite = restaurantMealInfo?.isFavorite?.invoke(userId) ?: false,
                    isFavorable = restaurantMealInfo?.isFavorable?.invoke(userId) ?: !isMealOwner
            ),
            votes = restaurantMealInfo?.let {
                toVotesOutput(
                        isVotable = it.isFavorable(userId),
                        votes = it.votes.value,
                        userVote = it.userVote(userId)
                )
            } ?: toDefaultVotesOutput(isVotable = isMealOwner),
            isSuggested = !meal.isUserMeal(),
            isVerified = restaurantMealInfo?.isVerified ?: false,
            isReportable = restaurantMealInfo?.isReportable?.invoke(userId) ?: !isMealOwner
    )
}