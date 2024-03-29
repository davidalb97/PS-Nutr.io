package pt.isel.ps.g06.httpserver.dataAccess.output.ingredient

import pt.isel.ps.g06.httpserver.model.MealIngredient

data class IngredientsContainerOutput(
        val ingredients: Collection<DetailedIngredientOutput>
)

fun toIngredientsContainerOutput(
        ingredients: Collection<MealIngredient>
): IngredientsContainerOutput {
    return IngredientsContainerOutput(ingredients = ingredients.map(::toDetailedIngredientOutput))
}