package pt.isel.ps.g06.httpserver.dataAccess.output.ingredient

import pt.isel.ps.g06.httpserver.model.food.Ingredient

data class IngredientsContainerOutput(
        val ingredients: Collection<DetailedIngredientOutput>
)

fun toIngredientsContainerOutput(
        ingredients: Collection<Ingredient>,
        userId: Int? = null
): IngredientsContainerOutput {
    return IngredientsContainerOutput(ingredients = ingredients.map { toDetailedIngredientOutput(it, userId) })
}