package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbSubmission

interface SubmissionDao {

    @SqlQuery("SELECT * FROM DbSubmission")
    fun getSubmissions(): List<DbSubmission>

    @SqlQuery("SELECT * FROM DbSubmission WHERE submission_id = :submissionId")
    fun getSubmission(submissionId: Int): Int

    @SqlQuery("INSERT INTO DbSubmission(submission_type) VALUES(:submission_type)")
    @GetGeneratedKeys
    fun insertSubmission(@Bind submission_type: String): Int
}