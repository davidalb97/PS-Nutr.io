package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionSubmitterDto

interface SubmissionSubmitterDao {

    companion object {
        const val table = "SubmissionSubmitter"
        const val submissionId = "submission_id"
        const val submitterId = "submitter_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbSubmissionSubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getAllBySubmissionId(submissionId: Int): List<DbSubmissionSubmitterDto>

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitter_id")
    fun getAllBySubmitterId(submitterId: Int): List<DbSubmissionSubmitterDto>

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId)" +
            " VALUES(:submissionId, :submitterId) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind submitterId: Int): DbSubmissionSubmitterDto

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId) values <submissionSubmitterParams> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [submissionId, submitterId])
                  submissionSubmitterParams: List<SubmissionSubmitterParam>
    ): List<DbSubmissionSubmitterDto>

    @SqlQuery("DELETE FROM $table WHERE $submissionId = :submissionId RETURNING *")
    fun deleteAllBySubmissionId(submissionId: Int): List<DbSubmissionSubmitterDto>
}

data class SubmissionSubmitterParam(
        val submission_id: Int,
        val submitter_id: Int
)