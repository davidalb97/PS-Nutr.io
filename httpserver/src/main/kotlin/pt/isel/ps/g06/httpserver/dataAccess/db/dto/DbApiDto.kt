package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class DbApiDto(
        val submitter_id: Int,
        val api_name: String,
        val api_token: String
)