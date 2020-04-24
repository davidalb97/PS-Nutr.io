package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class ReportDto(
        val report_submission_id: Int,
        val submission_id: Int,
        val description: String
)