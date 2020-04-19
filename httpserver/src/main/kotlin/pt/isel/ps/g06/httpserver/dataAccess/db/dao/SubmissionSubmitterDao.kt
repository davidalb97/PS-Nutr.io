package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmission
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmissionSubmitter

private const val table = "SubmissionSubmitter"
private const val submissionId = "submission_id"
private const val submitterId = "submitter_id"
private const val submissionDate = "submission_date"

interface SubmissionSubmitterDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbSubmissionSubmitter>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getBySubmissionId(submissionId: Int): DbSubmissionSubmitter

    @SqlQuery("SELECT * FROM $table WHERE $submitterId = :submitter_id")
    fun getBySubmitterId(submitterId: Int): DbSubmissionSubmitter

    @SqlQuery("INSERT INTO $table($submissionId, $submitterId) VALUES(:submission_id, :submitter_id)")
    fun insert(@Bind submission_id: String, @Bind submitter_id: String): Int
}