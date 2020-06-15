package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import java.sql.Timestamp

open class DbSubmitterDto(
        val submitter_id: Int,
        val submitter_name: String,
        val creation_date: Timestamp,
        val submitter_type: String
)