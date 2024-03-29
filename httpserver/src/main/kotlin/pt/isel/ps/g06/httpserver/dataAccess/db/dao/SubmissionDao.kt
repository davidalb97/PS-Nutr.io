package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.row.DbRowSubmissionMapper

@RegisterRowMapper(DbRowSubmissionMapper::class)
interface SubmissionDao {

    companion object {
        const val table = "Submission"
        const val type = "submission_type"
        const val id = "submission_id"
        const val date = "submission_date"
    }

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(submissionId: Int): DbSubmissionDto?

    @SqlQuery("INSERT INTO $table($type) VALUES(:submission_type) RETURNING *")
    fun insert(@Bind submission_type: String): DbSubmissionDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): DbSubmissionDto
}

//Variable names must match sql columns!!!
data class SubmissionParam(val submission_type: String)