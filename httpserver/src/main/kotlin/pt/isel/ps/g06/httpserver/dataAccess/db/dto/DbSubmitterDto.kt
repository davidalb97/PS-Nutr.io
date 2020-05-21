package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class DbSubmitterDto(
        val submitter_id: Int,
        val submitter_name: String,
        val submitter_type: String
)