package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionSubmitterDto

private const val table = "SubmissionSubmitter"
private const val submissionId = "submission_id"
private const val submitterId = "submitter_id"

interface SubmissionSubmitterDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmissionSubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getBySubmissionId(submissionId: Int): SubmissionSubmitterDto?

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitter_id")
    fun getBySubmitterId(submitterId: Int): SubmissionSubmitterDto?

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId)" +
            " VALUES(:submission_id, :submitter_id) RETURNING *")
    fun insert(@Bind submission_id: Int, @Bind submitter_id: Int): SubmissionSubmitterDto

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [submissionId, submitterId]
    ) values: List<SubmissionSubmitterParam>): List<SubmissionSubmitterDto>
}

data class SubmissionSubmitterParam(
        val submission_id: Int,
        val submitter_id: Int
)