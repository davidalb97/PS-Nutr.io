package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import java.time.OffsetDateTime

data class DbSubmissionSubmitterDto(
        val submission_id: Int,
        val submitter_id: Int
)