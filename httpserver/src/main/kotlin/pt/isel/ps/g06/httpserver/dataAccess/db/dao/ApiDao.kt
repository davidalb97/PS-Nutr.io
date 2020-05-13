package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ApiDto

//Submitter table constants
private const val submitterTable = SubmitterDao.table
private const val submitterName = SubmitterDao.name
private const val submitterId = SubmitterDao.id

//SubmissionSubmitter table constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

interface ApiDao {

    companion object {
        const val table = "Api"
        const val id = "submitter_id"
        const val apiToken = "api_token"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<ApiDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submitterId")
    fun getById(@Bind submitterId: Int): ApiDto?

    @SqlQuery("SELECT $table.$id, $table.$apiToken" +
            " FROM $table" +
            " INNER JOIN $SS_table" +
            " ON $SS_table.$SS_submitterId = $table.$id" +
            " WHERE $SS_table.$SS_submissionId = :submissionId"
    )
    fun getSubmitterBySubmissionId(submissionId: Int): ApiDto?

    @SqlQuery("SELECT $submitterTable.$submitterId, $table.$apiToken" +
            " FROM $submitterTable" +
            " INNER JOIN $table" +
            " ON $submitterTable.$submitterId = $table.$id" +
            " WHERE $submitterName = :submitterName"
    )
    fun getByName(@Bind submitterName: String): ApiDto?

    @SqlQuery("INSERT INTO $table($id, $apiToken) " +
            "VALUES(:submitterId, :apiToken) RETURNING *")
    fun insert(@Bind submitterId: Int, @Bind apiToken: String): ApiDto
}