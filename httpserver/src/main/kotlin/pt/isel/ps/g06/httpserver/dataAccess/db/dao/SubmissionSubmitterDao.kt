package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmission
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmissionSubmitter

interface SubmissionSubmitterDao {
    @SqlQuery("SELECT * FROM DbSubmissionSubmitter")
    fun getSubmissions(): List<DbSubmissionSubmitter>

    @SqlQuery("SELECT * FROM DbSubmissionSubmitter WHERE submission_id = :submissionId")
    fun getSubmission(submissionId: Int): DbSubmissionSubmitter

    @SqlQuery("SELECT * FROM DbSubmissionSubmitter WHERE submitter_id = :submitter_id")
    fun getSubmissionsFromSubmitter(submitterId: Int): List<DbSubmissionSubmitter>

    @SqlQuery("INSERT INTO DbSubmission(submission_type) VALUES(:submissionType)")
    @GetGeneratedKeys
    fun insertSubmission(@Bind submissionType: String): Int
}