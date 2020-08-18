package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.REPORTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportDto
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidInputDomain
import pt.isel.ps.g06.httpserver.common.exception.clientError.NonReportableSubmissionException

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val reportDaoClass = ReportDao::class.java

@Repository
class ReportDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getAll(): Collection<DbReportDto> {
        return jdbi.inTransaction<Collection<DbReportDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getAll()
        }
    }

    fun getAllFromSubmission(submissionId: Int): Collection<DbReportDto> {
        return jdbi.inTransaction<Collection<DbReportDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getAllBySubmission(submissionId)
        }
    }

    fun insert(
            submitterId: Int,
            submissionId: Int,
            report: String
    ): DbReportDto {
        return jdbi.inTransaction<DbReportDto, Exception>(isolationLevel) {

            // Check if the submission exists and it is reportable
            if (!requireContract(submitterId, REPORTABLE, isolationLevel)) {
                throw NonReportableSubmissionException()
            }

            return@inTransaction it.attach(reportDaoClass)
                    .insert(submitterId, submissionId, report)
        }
    }
}