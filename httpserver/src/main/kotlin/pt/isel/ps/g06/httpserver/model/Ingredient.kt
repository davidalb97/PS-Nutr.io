package pt.isel.ps.g06.httpserver.model

//TODO This should extend Meal
data class Ingredient(
        val name: String,
        val id: String,
        val carbs: Int?
)