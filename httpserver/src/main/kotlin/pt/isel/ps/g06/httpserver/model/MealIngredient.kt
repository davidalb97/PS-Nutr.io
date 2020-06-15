package pt.isel.ps.g06.httpserver.model

import java.net.URI

data class MealIngredient(
        val identifier: Int,
        val name: String,
        val isFavorite: (Int) -> Boolean,
        val imageUri: URI?,
        val nutritionalValues: NutritionalValues
)