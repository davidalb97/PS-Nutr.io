package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.REPORTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ReportDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val reportDaoClass = ReportDao::class.java

@Repository
class ReportDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun insert(
            submitterId: Int,
            submission_id: Int,
            report: String
    ): ReportDto {
        return jdbi.inTransaction<ReportDto, Exception>(isolationLevel) {

            // Check if the submission exists and it is votable
            requireContract(submitterId, REPORTABLE, isolationLevel)

            return@inTransaction it.attach(reportDaoClass)
                    .insert(submitterId, submission_id, report)
        }
    }
}