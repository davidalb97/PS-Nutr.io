package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmission

private const val table = "Submission"
private const val type = "submission_type"
private const val id = "submission_id"

interface SubmissionDao {

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbSubmission>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(submissionId: Int): DbSubmission

    @SqlQuery("INSERT INTO $table($type) VALUES(:submission_type)")
    @GetGeneratedKeys
    fun insert(@Bind submission_type: String): Int

    @SqlUpdate("INSERT INTO $table($type) values <values>")
    @GetGeneratedKeys
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [type]
    ) vararg values: SubmissionParam): List<Int>
}

//Variable names must match sql columns!!!
data class SubmissionParam(val submission_type: String)