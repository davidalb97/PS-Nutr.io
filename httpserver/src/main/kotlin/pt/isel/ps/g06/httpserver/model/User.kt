package pt.isel.ps.g06.httpserver.model

import java.net.URI
import java.time.OffsetDateTime

data class User(
        val name: String,
        val creationDate: OffsetDateTime,
        val image: URI?
)