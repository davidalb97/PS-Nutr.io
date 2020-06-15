package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI

data class SimplifiedMealOutput(
        val mealIdentifier: Int,
        val name: String,
        val isFavorite: Boolean,
        val isSuggested: Boolean,
        val imageUri: URI?
)

fun toSimplifiedMealOutput(meal: Meal, userId: Int? = null): SimplifiedMealOutput {
    return SimplifiedMealOutput(
            mealIdentifier = meal.identifier,
            name = meal.name,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            isSuggested = !meal.isUserMeal(),
            imageUri = meal.imageUri
    )
}