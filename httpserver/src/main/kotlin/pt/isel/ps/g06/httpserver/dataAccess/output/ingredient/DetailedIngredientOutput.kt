package pt.isel.ps.g06.httpserver.dataAccess.output.ingredient

import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.model.MealIngredient
import java.net.URI

data class DetailedIngredientOutput(
        val id: Int,
        val name: String,
        val imageUri: URI?,
        val isFavorite: Boolean,
        val nutritionalInfo: NutritionalInfoOutput
)

fun toDetailedIngredientOutput(ingredient: MealIngredient, userId: Int? = null): DetailedIngredientOutput {
    return DetailedIngredientOutput(
            id = ingredient.identifier,
            name = ingredient.name,
            imageUri = ingredient.imageUri,
            isFavorite = userId?.let { ingredient.isFavorite(userId) } ?: false,
            nutritionalInfo = toNutritionalInfoOutput(ingredient.nutritionalValues)
    )
}