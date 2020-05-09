package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ReportDto

interface ReportDao {

    companion object {
        const val table = "Report"
        const val submissionId = "submission_id"
        const val reporterId = "submitter_id"
        const val description = "description"
    }

    @SqlQuery("INSERT INTO $table($reporterId, $submissionId, $description) " +
            "VALUES(:reporterSubmitterId, :submissionId, :description) RETURNING *")
    fun insert(@Bind reporterSubmitterId: Int, submissionId: Int, description: String): ReportDto

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteAllBySubmissionId(submissionId: Int): List<ReportDto>
}