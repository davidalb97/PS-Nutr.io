package pt.isel.ps.g06.httpserver.model

data class Meal(
        val identifier: Int,
        val name: String,
        val votes: Votes
)