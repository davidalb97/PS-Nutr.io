package pt.isel.ps.g06.httpserver.model

import java.util.stream.Stream

data class MealComposition(
        val meals: Stream<Meal>,
        val ingredients: Stream<MealIngredient>
)