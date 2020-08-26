package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class DbReportDto(
        val report_id: Int,
        val submitter_id: Int,
        val submission_id: Int,
        val description: String
)