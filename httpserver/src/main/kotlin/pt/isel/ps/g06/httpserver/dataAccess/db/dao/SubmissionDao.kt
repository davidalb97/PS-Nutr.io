package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmission

private const val submissionTable = "Submission"
private const val submissionType = "submission_type"
private const val submissionId = "submission_id"

interface SubmissionDao {

    @SqlQuery("SELECT * FROM $submissionTable")
    fun getSubmissions(): List<DbSubmission>

    @SqlQuery("SELECT * FROM $submissionTable WHERE $submissionId = :submissionId")
    fun getSubmissionById(submissionId: Int): DbSubmission

    @SqlQuery("INSERT INTO $submissionTable($submissionType) VALUES(:submission_type)")
    @GetGeneratedKeys
    fun insertSubmission(@Bind submission_type: String): Int
}