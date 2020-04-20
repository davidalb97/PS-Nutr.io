package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
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

    @SqlUpdate("INSERT INTO $table($submissionId, $apiId, $submissionType) values <values>")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [submissionId, apiId, submissionType]
    ) vararg values: ApiSubmissionParam)
}

data class ApiSubmissionParam(
        val submission_id: Int,
        val apiId: Int,
        val submission_type: String
)