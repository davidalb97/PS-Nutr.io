package pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import pt.isel.ps.g06.httpserver.dataAccess.output.*
import pt.isel.ps.g06.httpserver.dataAccess.output.cuisines.CuisinesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.cuisines.toSimplifiedCuisinesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.MealCompositionOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.toMealComposition
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.ICuisinesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.SimplifiedUserOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.toSimplifiedUserOutput
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import java.net.URI
import java.time.OffsetDateTime

class DetailedRestaurantMealOutput(
        identifier: Int,
        name: String,
        image: URI?,
        votes: VotesOutput,
        favorites: FavoritesOutput,
        isSuggested: Boolean,
        isReportable: Boolean,
        nutritionalInfo: NutritionalInfoOutput,
        isVerified: Boolean,
        @JsonSerialize(using = ToStringSerializer::class)
        val creationDate: OffsetDateTime?,
        val createdBy: SimplifiedUserOutput?,
        val composedBy: MealCompositionOutput?,
        val portions: PortionsOutput,
        override val cuisines: CuisinesOutput
) : SimplifiedRestaurantMealOutput(
        identifier = identifier,
        name = name,
        image = image,
        favorites = favorites,
        isSuggested = isSuggested,
        votes = votes,
        isReportable = isReportable,
        isVerified = isVerified,
        nutritionalInfo = nutritionalInfo
), ICuisinesOutput

fun toDetailedRestaurantMealOutput(restaurantMeal: RestaurantMeal, userId: Int? = null): DetailedRestaurantMealOutput {
    val meal = restaurantMeal.meal
    val restaurantMealInfo = restaurantMeal.info
    val isMealOwner = userId?.let { meal.submitterInfo.value?.identifier == userId } ?: false
    val defaultVotable = !isMealOwner
    val defaultFavorable = !isMealOwner
    val defaultReportable = !meal.isSuggestedMeal(restaurantMeal.restaurant) && !isMealOwner
    return DetailedRestaurantMealOutput(
            identifier = meal.identifier,
            name = meal.name,
            image = meal.image,
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalInfo),
            //All RestaurantMeal info is only available when inserted on the database
            favorites = FavoritesOutput(
                    isFavorite = restaurantMealInfo?.isFavorite?.invoke(userId) ?: false,
                    isFavorable = restaurantMealInfo?.isFavorable?.invoke(userId) ?: defaultFavorable
            ),
            votes = restaurantMealInfo?.let {
                toVotesOutput(
                        isVotable = it.isVotable(userId),
                        votes = it.votes.value,
                        userVote = it.userVote(userId)
                )
            } ?: toDefaultVotesOutput(isVotable = defaultVotable),
            isSuggested = !meal.isUserMeal(),
            isVerified = restaurantMealInfo?.isVerified ?: false,
            isReportable = restaurantMealInfo?.isReportable?.invoke(userId) ?: defaultReportable,
            //Info values bellow:
            createdBy = meal.submitterInfo.value?.let(::toSimplifiedUserOutput),
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            portions = toPortionsOutput(restaurantMeal, userId),
            cuisines = toSimplifiedCuisinesOutput(restaurantMeal.meal.cuisines.toList())
    )
}