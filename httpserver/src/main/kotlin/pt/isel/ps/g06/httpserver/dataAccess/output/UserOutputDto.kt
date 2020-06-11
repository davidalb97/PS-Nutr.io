package pt.isel.ps.g06.httpserver.dataAccess.output

import java.time.OffsetDateTime

data class UserOutputDto(
        val id: Int,
        val name: Int,
        val image: String,
        val creationDate: OffsetDateTime
)