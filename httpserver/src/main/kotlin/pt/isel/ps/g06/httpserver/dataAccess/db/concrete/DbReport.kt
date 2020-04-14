package pt.isel.ps.g06.httpserver.dataAccess.db.concrete

data class DbReport(
        val report_submission_id: Int,
        val submission_id: Int,
        val description: String
)