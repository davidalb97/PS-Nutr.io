package pt.isel.ps.g06.httpserver.model

import java.net.URI

data class MealIngredient(
        val submissionId: Int,
        val name: String,
        val nutritionalValues: NutritionalValues,
        val image: URI?,
        val isFavorite: (Int) -> Boolean
)