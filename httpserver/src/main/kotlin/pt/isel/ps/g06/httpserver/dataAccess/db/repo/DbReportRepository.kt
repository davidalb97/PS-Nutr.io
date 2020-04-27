package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ReportDto

class DbReportRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE

    fun addReport(
            submitterId: Int,
            submission_id: Int,
            report: String
    ): ReportDto? {
        return inTransaction(jdbi, serializable) {
            //validateSubmitterId(it, submitterId)

            // Check if the submission exists
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(submission_id)

            if (submissionDto != null) {
                // Submit a report to that Submission
                return@inTransaction it.attach(ReportDao::class.java)
                        .insert(submitterId, submission_id, report)
            }

            null
        }
    }
}