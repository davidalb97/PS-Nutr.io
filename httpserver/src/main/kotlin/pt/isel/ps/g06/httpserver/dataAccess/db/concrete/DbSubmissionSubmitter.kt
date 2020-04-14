package pt.isel.ps.g06.httpserver.dataAccess.db.concrete

import java.time.OffsetDateTime

data class DbSubmissionSubmitter(
        val submission_id: Int,
        val submitter_id: Int,
        val submission_date: OffsetDateTime
)