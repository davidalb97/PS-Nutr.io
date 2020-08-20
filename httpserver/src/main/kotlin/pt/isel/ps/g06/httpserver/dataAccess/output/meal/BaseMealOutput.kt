package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI

open class BaseMealOutput(
        val mealIdentifier: Int,
        val name: String,
        val isFavorite: Boolean,
        val isSuggested: Boolean,
        val isVotable: Boolean,
        val imageUri: URI?
)

fun toBaseMealOutput(meal: Meal, userId: Int? = null): BaseMealOutput {
    return BaseMealOutput(
            mealIdentifier = meal.identifier,
            name = meal.name,
            isFavorite = userId?.let { meal.isFavorite(userId) } ?: false,
            isSuggested = !meal.isUserMeal(),
            //A suggested/custom meal is not votable
            isVotable = false,
            imageUri = meal.imageUri
    )
}