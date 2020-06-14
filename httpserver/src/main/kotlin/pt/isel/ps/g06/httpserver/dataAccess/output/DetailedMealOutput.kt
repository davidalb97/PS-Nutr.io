package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI
import java.time.OffsetDateTime

data class DetailedMealOutput(
        val id: Int,
        val name: String,
        val imageUri: URI?,
        val isFavorite: Boolean,
        val creationDate: OffsetDateTime?,
        val composedBy: MealCompositionOutput?,
        val nutritionalInfo: NutritionalInfoOutput,
        val createdBy: SimplifiedUserOutput?
)

fun toDetailedMealOutput(meal: Meal, userId: Int? = null): DetailedMealOutput {
    return DetailedMealOutput(
            id = meal.identifier,
            name = meal.name,
            imageUri = meal.imageUri,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalValues),
            createdBy = meal.creatorInfo.value?.let { toSimplifiedUserOutput(it) }
    )
}