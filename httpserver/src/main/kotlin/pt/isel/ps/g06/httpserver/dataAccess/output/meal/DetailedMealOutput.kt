package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.ISubmitterInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.SimplifiedUserOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.toSimplifiedUserOutput
import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI
import java.time.OffsetDateTime

class DetailedMealOutput(
        mealIdentifier: Int,
        name: String,
        imageUri: URI?,
        nutritionalInfo: NutritionalInfoOutput,
        isSuggested: Boolean,
        isVerified: Boolean,
        favorites: FavoritesOutput,
        @JsonSerialize(using = ToStringSerializer::class)
        val creationDate: OffsetDateTime?,
        val composedBy: MealCompositionOutput?,
        override val createdBy: SimplifiedUserOutput?
) : BaseMealOutput(
        identifier = mealIdentifier,
        name = name,
        image = imageUri,
        nutritionalInfo = nutritionalInfo,
        isSuggested = isSuggested,
        isVerified = isVerified,
        favorites = favorites
), ISubmitterInfoOutput

fun toDetailedMealOutput(meal: Meal, userId: Int? = null): DetailedMealOutput {
    return DetailedMealOutput(
            mealIdentifier = meal.identifier,
            name = meal.name,
            imageUri = meal.image,
            favorites = FavoritesOutput(
                    isFavorite = meal.isFavorite(userId),
                    isFavorable = meal.isFavorable(userId)
            ),
            isSuggested = !meal.isUserMeal(),
            isVerified = false,
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalInfo),
            createdBy = meal.submitterInfo.value?.let(::toSimplifiedUserOutput)
    )
}