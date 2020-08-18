package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.REPORTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val reportDaoClass = ReportDao::class.java

@Repository
class ReportDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getAll(

    ): Collection<DbReportDto> {
        return jdbi.inTransaction<Collection<DbReportDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getAll()
        }
    }

    fun insert(
            submitterId: Int,
            submissionId: Int,
            report: String
    ): DbReportDto {
        return jdbi.inTransaction<DbReportDto, Exception>(isolationLevel) {

            // Check if the submission exists and it is reportable
            requireContract(submitterId, REPORTABLE, isolationLevel)

            return@inTransaction it.attach(reportDaoClass)
                    .insert(submitterId, submissionId, report)
        }
    }
}