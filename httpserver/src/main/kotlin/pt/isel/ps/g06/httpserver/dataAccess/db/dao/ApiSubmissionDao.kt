package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ApiSubmissionDto

private const val table = "ApiSubmission"
private const val submissionId = "submission_id"
private const val apiId = "apiId"
private const val submissionType = "submission_type"

interface ApiSubmissionDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<ApiSubmissionDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getById(submissionId: Int): ApiSubmissionDto?

    @SqlQuery("SELECT * FROM $table WHERE $apiId = :apiId")
    fun getAllByApiId(apiId: Int): List<ApiSubmissionDto>

    @SqlQuery("INSERT INTO $table($submissionId, $apiId, $submissionType)" +
            " VALUES(:submissionId, :apiId, :submissionType) RETURNING *")
    fun insert(@Bind submissionId: Int, apiId: Int, submissionType: String): ApiSubmissionDto

    @SqlQuery("INSERT INTO $table($submissionId, $apiId, $submissionType) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [submissionId, apiId, submissionType]
    ) values: List<ApiSubmissionParam>): List<ApiSubmissionDto>
}

data class ApiSubmissionParam(
        val submission_id: Int,
        val apiId: Int,
        val submission_type: String
)