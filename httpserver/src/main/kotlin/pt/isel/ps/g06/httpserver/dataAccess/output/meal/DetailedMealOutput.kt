package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.SimplifiedUserOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.vote.toSimplifiedUserOutput
import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI
import java.time.OffsetDateTime

class DetailedMealOutput(
        mealIdentifier: Int,
        name: String,
        imageUri: URI?,
        isFavorite: Boolean,
        nutritionalInfo: NutritionalInfoOutput,
        isSuggested: Boolean,
        isVerified: Boolean,
        isVotable: Boolean,
        @JsonSerialize(using = ToStringSerializer::class)
        val creationDate: OffsetDateTime?,
        val composedBy: MealCompositionOutput?,
        val createdBy: SimplifiedUserOutput?
) : BaseMealOutput(
        mealIdentifier = mealIdentifier,
        name = name,
        imageUri = imageUri,
        nutritionalInfo = nutritionalInfo,
        isSuggested = isSuggested,
        isFavorite = isFavorite,
        isVerified = isVerified,
        isVotable = isVotable
)

fun toDetailedMealOutput(meal: Meal, userId: Int? = null): DetailedMealOutput {
    return DetailedMealOutput(
            mealIdentifier = meal.identifier,
            name = meal.name,
            imageUri = meal.imageUri,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            isSuggested = !meal.isUserMeal(),
            //A suggested/custom meal is not votable
            isVotable = false,
            isVerified = false,
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalValues),
            createdBy = meal.submitterInfo.value?.let { toSimplifiedUserOutput(it) }
    )
}