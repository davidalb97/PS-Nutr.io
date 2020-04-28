package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import java.time.OffsetDateTime

data class UserDto(
        val submitter_id: Int,
        val email: String,
        val session_secret: String,
        val creation_date: OffsetDateTime
)