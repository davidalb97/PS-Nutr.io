package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ReportDto

private const val table = "Report"
private const val submission_id = "submission_id"
private const val reporter_id = "report_submission_id"
private const val description = "description"

interface ReportDao {

    @SqlQuery("INSERT INTO $table($reporter_id, $submission_id, $description) " +
            "VALUES(:reporterSubmitterId, :submissionId, :description) RETURNING *")
    fun insert(@Bind reporterSubmitterId: Int, submissionId: Int, description: String): ReportDto
}