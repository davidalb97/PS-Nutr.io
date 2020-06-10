package pt.isel.ps.g06.httpserver.model

import java.net.URI

data class Meal(
        val identifier: String,
        val name: String,
        val votes: Votes,
        val image: URI?,
        val info: Lazy<MealInfo>
)