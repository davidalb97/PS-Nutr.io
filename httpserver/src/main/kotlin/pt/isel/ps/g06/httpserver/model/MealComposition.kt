package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.MealIngredient

data class MealComposition(
        val meals: Sequence<Meal>,
        val ingredients: Sequence<MealIngredient>
)