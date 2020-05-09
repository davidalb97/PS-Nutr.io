package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ApiSubmissionDto

//SubmissionSubmitter constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

//Submitter constants
private const val S_table = SubmitterDao.table
private const val S_submissionId = SubmitterDao.id
private const val S_type = SubmitterDao.type

interface ApiSubmissionDao {

    companion object {
        const val table = "ApiSubmission"
        const val submissionId = "submission_id"
        const val apiId = "apiId"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<ApiSubmissionDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getBySubmissionId(submissionId: Int): ApiSubmissionDto?

    @SqlQuery("SELECT $table.$submissionId, $table.$apiId" +
            " FROM $table" +
            " INNER JOIN $SS_table" +
            " ON $table.$submissionId = $SS_table.$SS_submissionId" +
            " INNER JOIN $S_table" +
            " ON $table.$submissionId = $S_table.$S_submissionId" +
            " WHERE $SS_table.$SS_submitterId = :submitterId" +
            " AND $S_table.$S_type = :submissionType" +
            " AND $table.$apiId in (<values>)"
    )
    fun getAllBySubmitterIdTypeAndApiIds(
            @Bind submitterId: Int,
            @Bind submissionType: String,
            @BindList("values") apiIds: List<Int>
    ): List<ApiSubmissionDto>

    @SqlQuery("SELECT $table.$submissionId, $table.$apiId" +
            " FROM $table" +
            " INNER JOIN $SS_table" +
            " ON $table.$submissionId = $SS_table.$SS_submissionId" +
            " INNER JOIN $S_table" +
            " ON $table.$submissionId = $S_table.$S_submissionId" +
            " WHERE $SS_table.$SS_submitterId = :submitterId" +
            " AND $S_table.$S_type = :submissionType"
    )
    fun getAllBySubmitterIdAndType(
            @Bind submitterId: Int,
            @Bind submissionType: String
    ): List<ApiSubmissionDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId in (<values>)")
    fun getAllBySubmissionIds(
            @BindList("values") submissionIds: List<Int>
    ): List<ApiSubmissionDto>

    @SqlQuery("SELECT * FROM $table WHERE $apiId = :apiId")
    fun getAllByApiId(apiId: Int): List<ApiSubmissionDto>

    @SqlQuery("INSERT INTO $table($submissionId, $apiId)" +
            " VALUES(:submissionId, :apiId) RETURNING *")
    fun insert(@Bind submissionId: Int, apiId: Int): ApiSubmissionDto

    @SqlQuery("INSERT INTO $table($submissionId, $apiId)" +
            " values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [submissionId, apiId]
    ) values: List<ApiSubmissionParam>): List<ApiSubmissionDto>

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteById(submissionId: Int): ApiSubmissionDto
}

data class ApiSubmissionParam(
        val submission_id: Int,
        val apiId: Int
)