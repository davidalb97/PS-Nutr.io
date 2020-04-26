package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionDto

private const val table = "Submission"
private const val type = "submission_type"
private const val id = "submission_id"

interface SubmissionDao {

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmissionDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(submissionId: Int): SubmissionDto?

    @SqlQuery("INSERT INTO $table($type) VALUES(:submission_type) RETURNING *")
    fun insert(@Bind submission_type: String): SubmissionDto

    @SqlQuery("INSERT INTO $table($type) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [type]
    ) values: List<SubmissionParam>): List<SubmissionDto>
}

//Variable names must match sql columns!!!
data class SubmissionParam(val submission_type: String)