package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import java.time.OffsetDateTime

data class DbSubmissionDto(
        val submission_id: Int,
        val submission_type: String,
        val submission_date: OffsetDateTime
)