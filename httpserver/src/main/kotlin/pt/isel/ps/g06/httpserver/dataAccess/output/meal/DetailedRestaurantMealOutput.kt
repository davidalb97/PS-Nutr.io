package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.*
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.VoteState
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
        val creationDate: OffsetDateTime?,
        val composedBy: MealCompositionOutput?,
        val portions: Collection<Int>,
        @JsonSerialize(using = ToStringSerializer::class)
        val createdBy: SimplifiedUserOutput?
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
)

fun toDetailedRestaurantMealOutput(restaurantMeal: RestaurantMeal, userId: Int? = null): DetailedRestaurantMealOutput {
    val meal = restaurantMeal.meal
    val restaurantMealInfo = restaurantMeal.info
    val isMealOwner = meal.isUserMeal() && userId?.let { meal.submitterInfo.value?.identifier == userId } ?: false
    return DetailedRestaurantMealOutput(
            identifier = meal.identifier,
            name = meal.name,
            image = meal.image,
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalInfo),
            //All RestaurantMeal info is only available when inserted on the database
            favorites = FavoritesOutput(
                    isFavorite = restaurantMealInfo?.isFavorite?.invoke(userId) ?: false,
                    isFavorable = restaurantMealInfo?.isFavorable?.invoke(userId) ?: !isMealOwner
            ),
            votes = restaurantMealInfo?.let {
                toVotesOutput(
                        isVotable = it.isVotable(userId),
                        votes = it.votes.value,
                        userVote = it.userVote(userId)
                )
            } ?: toDefaultVotesOutput(isVotable = isMealOwner),
            isSuggested = !meal.isUserMeal(),
            isVerified = restaurantMealInfo?.isVerified ?: false,
            isReportable = restaurantMealInfo?.isReportable?.invoke(userId) ?: isMealOwner,
            //Info values bellow:
            createdBy = meal.submitterInfo.value?.let(::toSimplifiedUserOutput),
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            portions = restaurantMealInfo
                    ?.portions
                    ?.map { portion -> portion.amount }
                    ?.toList()
                    ?: emptyList()
    )
}