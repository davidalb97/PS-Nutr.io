package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionSubmitterDto

interface SubmissionSubmitterDao {

    companion object {
        const val table = "SubmissionSubmitter"
        const val submissionId = "submission_id"
        const val submitterId = "submitter_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmissionSubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getAllBySubmissionId(submissionId: Int): List<SubmissionSubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitter_id")
    fun getAllBySubmitterId(submitterId: Int): List<SubmissionSubmitterDto>

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId)" +
            " VALUES(:submissionId, :submitterId) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind submitterId: Int): SubmissionSubmitterDto

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [submissionId, submitterId]
    ) values: List<SubmissionSubmitterParam>): List<SubmissionSubmitterDto>

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteAllBySubmissionId(submissionId: Int): List<SubmissionSubmitterDto>
}

data class SubmissionSubmitterParam(
        val submission_id: Int,
        val submitter_id: Int
)