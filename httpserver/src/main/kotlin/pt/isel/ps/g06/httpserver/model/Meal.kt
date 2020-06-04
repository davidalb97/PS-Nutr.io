package pt.isel.ps.g06.httpserver.model

import java.net.URI

data class Meal(
        val identifier: String,
        val name: String,
        val image: URI?,
        val info: Lazy<MealInfo>
)