package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.MealIngredient

data class IngredientsContainerOutput(
        val ingredients: Collection<DetailedIngredientOutput>
)

fun toIngredientsContainerOutput(
        ingredients: Collection<MealIngredient>,
        userId: Int? = null
): IngredientsContainerOutput {
    return IngredientsContainerOutput(ingredients = ingredients.map { toDetailedIngredientOutput(it, userId) })
}