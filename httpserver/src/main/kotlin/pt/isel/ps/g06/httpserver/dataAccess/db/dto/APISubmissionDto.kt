package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class APISubmissionDto(
        val submission_id: Int,
        val apiId: Int,
        val submission_type: String
)