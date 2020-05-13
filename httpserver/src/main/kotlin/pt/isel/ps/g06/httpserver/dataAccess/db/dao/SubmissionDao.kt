package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionDto

interface SubmissionDao {

    companion object {
        const val table = "Submission"
        const val type = "submission_type"
        const val id = "submission_id"
        const val date = "submission_date"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmissionDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(submissionId: Int): SubmissionDto?

    @SqlQuery("INSERT INTO $table($type) VALUES(:submission_type) RETURNING *")
    fun insert(@Bind submission_type: String): SubmissionDto

    @SqlQuery("INSERT INTO $table($type) values <submissionParams> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [type])
                  submissionParams: List<SubmissionParam>
    ): List<SubmissionDto>

    @SqlQuery("DELETE FROM $table($type) WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): SubmissionDto
}

//Variable names must match sql columns!!!
data class SubmissionParam(val submission_type: String)