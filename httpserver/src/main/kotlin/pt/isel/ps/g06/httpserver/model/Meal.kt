package pt.isel.ps.g06.httpserver.model

import java.net.URI

data class Meal(
        val identifier: Int,
        val name: String,
        val ingredients: Sequence<MealIngredient>,
        val carbs: Int,
        val image: URI? = null,
        val votes: Votes
)