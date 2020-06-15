package pt.isel.ps.g06.httpserver.dataAccess.output.meal

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
        isSuggested: Boolean,
        val creationDate: OffsetDateTime?,
        val composedBy: MealCompositionOutput?,
        val nutritionalInfo: NutritionalInfoOutput,
        val createdBy: SimplifiedUserOutput?
) : BaseMealOutput(
        mealIdentifier = mealIdentifier,
        name = name,
        imageUri = imageUri,
        isSuggested = isSuggested,
        isFavorite = isFavorite
)

fun toDetailedMealOutput(meal: Meal, userId: Int? = null): DetailedMealOutput {
    return DetailedMealOutput(
            mealIdentifier = meal.identifier,
            name = meal.name,
            imageUri = meal.imageUri,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            isSuggested = !meal.isUserMeal(),
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalValues),
            createdBy = meal.creatorInfo.value?.let { toSimplifiedUserOutput(it) }
    )
}