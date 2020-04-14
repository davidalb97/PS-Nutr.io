package pt.isel.ps.g06.httpserver.dataAccess.db.concrete

data class DbAPI_Submission(
        val submission_id: Int,
        val apiId: Int,
        val submission_type: String
)