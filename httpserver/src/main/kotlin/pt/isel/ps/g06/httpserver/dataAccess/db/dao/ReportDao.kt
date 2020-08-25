package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportDto

interface ReportDao {

    companion object {
        const val table = "Report"
        const val reportId = "report_id"
        const val submissionId = "submission_id"
        const val reporterId = "submitter_id"
        const val description = "description"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): Collection<DbReportDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getAllBySubmission(submissionId: Int): Collection<DbReportDto>

    @SqlQuery("INSERT INTO $table($reporterId, $submissionId, $description) " +
            "VALUES(:reporterSubmitterId, :submissionId, :description) RETURNING *")
    fun insert(@Bind reporterSubmitterId: Int, submissionId: Int, description: String): DbReportDto

    @SqlQuery("DELETE FROM $table WHERE $reportId = :reportId RETURNING *")
    fun deleteById(reportId: Int): DbReportDto
}