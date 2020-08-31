package pt.isel.ps.g06.httpserver.model

data class MealComposition(
        val meals: Sequence<Meal>,
        val ingredients: Sequence<MealIngredient>
)