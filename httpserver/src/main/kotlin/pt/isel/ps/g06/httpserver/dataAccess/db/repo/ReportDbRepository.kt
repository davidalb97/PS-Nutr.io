package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.clientError.DuplicateReportException
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidReportSubmissionException
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.REPORTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSimplifiedReportDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val reportDaoClass = ReportDao::class.java

@Repository
class ReportDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getAll(skip: Int?, count: Int?): Collection<DbReportDto> {
        return jdbi.inTransaction<Collection<DbReportDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getAll(skip, count)
        }
    }

    fun getAllBySubmissionType(submissionType: String, skip: Int?, count: Int?): Collection<DbSimplifiedReportDto> {
        return jdbi.inTransaction<Collection<DbSimplifiedReportDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getAllBySubmissionAndType(submissionType, skip, count)
        }
    }

    fun getAllFromSubmission(submissionId: Int): Collection<DbReportDto> {
        return jdbi.inTransaction<Collection<DbReportDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getAllBySubmission(submissionId)
        }
    }

    fun getReportFromSubmitter(submitterId: Int, submissionId: Int): DbReportDto? {
        return jdbi.inTransaction<DbReportDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getReportFromSubmitter(submitterId, submissionId)
        }
    }

    fun insert(
            submitterId: Int,
            submissionId: Int,
            report: String
    ): DbReportDto {
        return jdbi.inTransaction<DbReportDto, Exception>(isolationLevel) {

            ensureReportableContract(submissionId)

            ensureIsNotAlreadyReported(submitterId, submissionId)

            return@inTransaction it.attach(reportDaoClass)
                    .insert(submitterId, submissionId, report)
        }
    }

    fun delete(reportId: Int): DbReportDto {
        return jdbi.inTransaction<DbReportDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).deleteById(reportId)
        }
    }

    /**
     * Ensures if a submitter already reported the submission
     * @param   submissionId    The submission's identifier
     * @param   submitterId     The submitter's identifier
     */
    private fun ensureIsNotAlreadyReported(submitterId: Int, submissionId: Int) {
        if (getReportFromSubmitter(submitterId, submissionId) != null) {
            throw DuplicateReportException()
        }
    }

    /**
     * Ensures if the submission identifier is from a reported submission
     * @param   submissionId    The submission's identifier
     */
    private fun ensureReportableContract(submissionId: Int) {
        if (!hasContract(submissionId, REPORTABLE, isolationLevel)) {
            throw InvalidReportSubmissionException()
        }
    }
}