package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class DbReportSubmissionDto(
        val report_id: Int,
        val submitter_id: Int,
        val submission_id: Int,
        val description: String,
        val _submission_name: String,
        val submission_submitter: Int
)