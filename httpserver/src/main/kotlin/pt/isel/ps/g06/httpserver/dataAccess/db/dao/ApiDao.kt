package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiDto

//SubmissionSubmitter table constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

interface ApiDao {

    companion object {
        const val table = "Api"
        const val id = "submitter_id"
        const val name = "api_name"
        const val apiToken = "api_token"
    }

    @SqlQuery("SELECT * FROM $table WHERE $table.$name IN (<names>)")
    fun getApiSubmittersByName(@BindList names: Collection<String>): ResultIterable<DbApiDto>

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): ResultIterable<DbApiDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submitterId")
    fun getById(@Bind submitterId: Int): DbApiDto?

    @SqlQuery("SELECT * FROM $table WHERE $name = :apiName")
    fun getByName(@Bind apiName: String): DbApiDto?

    @SqlQuery("SELECT $table.$id, $table.$apiToken" +
            " FROM $table" +
            " INNER JOIN $SS_table" +
            " ON $SS_table.$SS_submitterId = $table.$id" +
            " WHERE $SS_table.$SS_submissionId = :submissionId"
    )
    fun getSubmitterBySubmissionId(submissionId: Int): DbApiDto?
}