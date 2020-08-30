package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.dataAccess.output.ingredient.DetailedIngredientOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.ingredient.toDetailedIngredientOutput
import pt.isel.ps.g06.httpserver.model.food.Meal

data class MealCompositionOutput(
        val meals: Collection<DetailedMealOutput>,
        val ingredients: Collection<DetailedIngredientOutput>
)

fun toMealComposition(meal: Meal): MealCompositionOutput {
    return MealCompositionOutput(
            meals = meal.composedBy.meals.map { toDetailedMealOutput(it) }.toList(),
            ingredients = meal.composedBy.ingredients.map { toDetailedIngredientOutput(it) }.toList()
    )
}