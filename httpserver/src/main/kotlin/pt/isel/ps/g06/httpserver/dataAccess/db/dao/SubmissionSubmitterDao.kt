package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionSubmitterDto

private const val table = "SubmissionSubmitter"
private const val submissionId = "submission_id"
private const val submitterId = "submitter_id"

interface SubmissionSubmitterDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmissionSubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getBySubmissionId(submissionId: Int): SubmissionSubmitterDto

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitter_id")
    fun getBySubmitterId(submitterId: Int): SubmissionSubmitterDto

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId) VALUES(:submission_id, :submitter_id)")
    fun insert(@Bind submission_id: Int, @Bind submitter_id: Int)

    @SqlUpdate("INSERT INTO $table($submissionId, $submitterId) values <values>")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [submissionId, submitterId]
    ) vararg values: SubmissionSubmitterParam)
}

data class SubmissionSubmitterParam(
        val submission_id: Int,
        val submitter_id: Int
)