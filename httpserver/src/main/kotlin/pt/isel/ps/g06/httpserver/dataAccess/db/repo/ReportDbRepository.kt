package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ReportDto

@Repository
class ReportDbRepository(private val jdbi: Jdbi) {

    private val serializable = TransactionIsolationLevel.SERIALIZABLE

    fun addReport(
            submitterId: Int,
            submission_id: Int,
            report: String
    ): ReportDto {
        return inTransaction(jdbi, serializable) {

            validateSubmissionId(it, submitterId)

            it.attach(ReportDao::class.java)
                    .insert(submitterId, submission_id, report)
        }
    }
}