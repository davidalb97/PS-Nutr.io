package pt.isel.ps.g06.httpserver.model

open class MealItem(
        val identifier: Int,
        val name: String,
        val isFavorite: Boolean,
        val image: String?
)