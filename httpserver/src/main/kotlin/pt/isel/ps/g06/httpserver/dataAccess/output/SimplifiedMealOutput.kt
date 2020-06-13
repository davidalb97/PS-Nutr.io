package pt.isel.ps.g06.httpserver.dataAccess.output

import java.net.URI

data class SimplifiedMealOutput(
        val id: Int,
        val name: String,
        val isFavorite: Boolean,
        val isSuggested: Boolean,
        val image: URI?
)