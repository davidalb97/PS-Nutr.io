package pt.isel.ps.g06.httpserver.model

import java.net.URI
import java.time.OffsetDateTime

data class Submitter(
        val identifier: Int,
        val name: String,
        val creationDate: OffsetDateTime?,
        val image: URI?
)