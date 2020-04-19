package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbAPI_Submission
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmission

interface ApiSubmissionDao {
    @SqlQuery("SELECT * FROM DbAPI_Submission")
    fun getApiSubmissions(): List<DbAPI_Submission>

    @SqlQuery("SELECT * FROM DbAPI_Submission WHERE submission_id = :submissionId")
    fun getApiSubmission(submissionId: Int): DbAPI_Submission

    @SqlQuery("SELECT * FROM DbAPI_Submission WHERE apiId = :apiId")
    fun getApiSubmissionFromSubmitter(apiId: Int): List<DbAPI_Submission>

    @SqlQuery("INSERT INTO DbAPI_Submission(submission_id, apiId, submission_type) VALUES(:submissionId, :apiId, :submissionType)")
    @GetGeneratedKeys
    fun insertSubmission(@Bind submissionId: Int, apiId: Int, submissionType: String): Int
}