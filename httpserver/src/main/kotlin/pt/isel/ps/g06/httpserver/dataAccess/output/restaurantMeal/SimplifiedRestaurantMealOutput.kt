package pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal

import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.BaseMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.IReportableOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.IVotableOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.VotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toDefaultVotesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toVotesOutput
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import java.net.URI

open class SimplifiedRestaurantMealOutput(
        //This is the MEAL identifier!
        identifier: Int,
        name: String,
        favorites: FavoritesOutput,
        isSuggested: Boolean,
        isVerified: Boolean,
        image: URI?,
        nutritionalInfo: NutritionalInfoOutput,
        override val isReportable: Boolean,
        override val votes: VotesOutput
) : BaseMealOutput(
        identifier = identifier,
        name = name,
        favorites = favorites,
        isSuggested = isSuggested,
        isVerified = isVerified,
        image = image,
        nutritionalInfo = nutritionalInfo
), IReportableOutput, IVotableOutput


fun toSimplifiedRestaurantMealOutput(
        restaurantMeal: RestaurantMeal,
        userId: Int? = null
): SimplifiedRestaurantMealOutput {
    val meal = restaurantMeal.meal
    val restaurantMealInfo = restaurantMeal.info
    val isMealOwner = meal.isUserMeal() && userId?.let { meal.submitterInfo.value?.identifier == userId } ?: false
    return SimplifiedRestaurantMealOutput(identifier = meal.identifier, name = meal.name, image = meal.image, nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalInfo), //All RestaurantMeal info is only available when inserted on the database
            favorites = FavoritesOutput(
                    isFavorite = restaurantMealInfo?.isFavorite?.invoke(userId) ?: false,
                    isFavorable = restaurantMealInfo?.isFavorable?.invoke(userId) ?: !isMealOwner
            ),
            votes = restaurantMealInfo?.let {
                toVotesOutput(
                        isVotable = it.isFavorable.invoke(userId),
                        votes = it.votes.value,
                        userVote = it.userVote(userId)
                )
            } ?: toDefaultVotesOutput(isVotable = isMealOwner),
            isSuggested = !meal.isUserMeal(),
            isVerified = restaurantMealInfo?.isVerified ?: false,
            isReportable = restaurantMealInfo?.isReportable?.invoke(userId) ?: !isMealOwner
    )
}