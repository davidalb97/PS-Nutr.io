package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbRowSubmitterMapper

//SubmissionSubmitter table constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

private const val API = "API"

@RegisterRowMapper(DbRowSubmitterMapper::class)
interface SubmitterDao {

    companion object {
        const val table = "Submitter"
        const val id = "submitter_id"
        const val date = "creation_date"
        const val type = "submitter_type"
    }

    @SqlQuery("SELECT $table.$id, $table.$type, $table.$date" +
            " FROM $table" +
            " INNER JOIN $SS_table" +
            " ON $table.$id = $SS_table.$SS_submitterId" +
            " WHERE $SS_table.$SS_submissionId = :submissionId")
    fun getSubmitterForSubmission(@Bind submissionId: Int): DbSubmitterDto?

    @SqlQuery("SELECT * FROM $table WHERE $id = :id")
    fun getSubmitterBySubmitterId(@Bind id: Int): DbSubmitterDto?

    @SqlQuery("INSERT INTO $table($type) VALUES(:type) RETURNING *")
    fun insertSubmitter(@Bind type: String): DbSubmitterDto
}