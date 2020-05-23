package pt.isel.ps.g06.httpserver.model

import java.util.stream.Stream

data class MealInfo(
        val carbohydrates: Float?,
        val ingredients: Stream<Ingredient>
)