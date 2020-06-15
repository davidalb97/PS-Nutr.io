package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import java.time.OffsetDateTime

class DbUserDto(
        submitter_id: Int,
        submitter_name: String,
        creation_date: OffsetDateTime,
        val email: String,
        val session_secret: String
) : DbSubmitterDto(
        submitter_id = submitter_id,
        submitter_name = submitter_name,
        submitter_type = "User",
        creation_date = creation_date
)