package pt.isel.ps.g06.httpserver.dataAccess.output

import java.time.OffsetDateTime

data class ItemMealOutputDto(
        val id: Int,
        val name: String,
        val image: String,
        val isFavorite: Boolean?,
        val creationDate: OffsetDateTime
)