package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import java.time.OffsetDateTime

open class DbSubmitterDto(
        val submitter_id: Int,
        val creation_date: OffsetDateTime,
        val submitter_type: String
)