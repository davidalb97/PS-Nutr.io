package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbAPI_Submission
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmission

private const val apiSubmission = "API_Submission"
private const val submissionId = "submission_id"
private const val apiId = "apiId"
private const val submissionType = "submission_type"

interface ApiSubmissionDao {
    @SqlQuery("SELECT * FROM $apiSubmission")
    fun getApiSubmissions(): List<DbAPI_Submission>

    @SqlQuery("SELECT * FROM $apiSubmission WHERE $submissionId = :submissionId")
    fun getApiSubmission(submissionId: Int): DbAPI_Submission

    @SqlQuery("SELECT * FROM $apiSubmission WHERE $apiId = :apiId")
    fun getApiSubmissionFromSubmitter(apiId: Int): List<DbAPI_Submission>

    @SqlQuery("INSERT INTO $apiSubmission($submissionId, $apiId, $submissionType) VALUES(:submissionId, :apiId, :submissionType)")
    @GetGeneratedKeys
    fun insertSubmission(@Bind submissionId: Int, apiId: Int, submissionType: String): Int
}