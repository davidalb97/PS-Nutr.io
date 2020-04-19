package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface ReportDao {

    @SqlQuery("INSERT INTO Report(report_submission_id, submission_id, description) " +
            "VALUES(:reportSubmissionId, :submissionId, :description)")
    fun insertVote(@Bind reportSubmissionId: Int, submissionId: Int, description: String): Int
}