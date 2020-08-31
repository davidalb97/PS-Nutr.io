package pt.isel.ps.g06.httpserver.dataAccess.db.dto

open class DbReportedSubmissionDto(
        val _submission_id: Int,
        val _submitter_id: Int,
        val _name: String,
        val _count: Int
)