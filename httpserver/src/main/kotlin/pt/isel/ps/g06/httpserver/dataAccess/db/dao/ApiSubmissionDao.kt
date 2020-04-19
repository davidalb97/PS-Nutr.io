package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbAPI_Submission

private const val table = "API_Submission"
private const val submissionId = "submission_id"
private const val apiId = "apiId"
private const val submissionType = "submission_type"

interface ApiSubmissionDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbAPI_Submission>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getById(submissionId: Int): DbAPI_Submission

    @SqlQuery("SELECT * FROM $table WHERE $apiId = :apiId")
    fun getAllByApiId(apiId: Int): List<DbAPI_Submission>

    @SqlQuery("INSERT INTO $table($submissionId, $apiId, $submissionType) VALUES(:submissionId, :apiId, :submissionType)")
    fun insert(@Bind submissionId: Int, apiId: Int, submissionType: String)
}