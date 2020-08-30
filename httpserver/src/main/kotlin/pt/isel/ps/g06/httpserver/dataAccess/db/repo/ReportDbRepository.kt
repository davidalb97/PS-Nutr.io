package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.problemJson.conflict.DuplicateReportException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.InvalidReportSubmissionException
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.REPORTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.*

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val reportDaoClass = ReportDao::class.java

@Repository
class ReportDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getAll(skip: Int?, count: Int?): Collection<DbReportSubmissionDto> {
        return jdbi.inTransaction<Collection<DbReportSubmissionDto>, Exception>(isolationLevel) {
            val dao = it.attach(reportDaoClass)
            val allReports = dao.getAll(skip, count)

            return@inTransaction allReports.map { reportDto ->
                val reportDetail = dao.getReportedSubmissionDetail(reportDto.submission_id)
                DbReportSubmissionDto(
                        report_id = reportDto.report_id,
                        submitter_id = reportDto.submitter_id,
                        submission_id = reportDto.submission_id,
                        description = reportDto.description,
                        _submission_name = reportDetail!!._submission_name,
                        submission_submitter = reportDetail.submitter_id
                )
            }
        }
    }

    fun getAllBySubmissionType(submissionType: String, skip: Int?, count: Int?): Collection<DbReportedSubmissionDto> {
        return jdbi.inTransaction<Collection<DbReportedSubmissionDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getAllReportedSubmissionsByType(submissionType, skip, count)
        }
    }

    fun getAllFromSubmission(submissionId: Int): Collection<DbSubmissionReportDto> {
        return jdbi.inTransaction<Collection<DbSubmissionReportDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getAllBySubmission(submissionId)
        }
    }

    fun getReportedSubmissionDetail(submissionId: Int): DbReportSubmissionDetailDto? {
        return jdbi.inTransaction<DbReportSubmissionDetailDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getReportedSubmissionDetail(submissionId)
        }
    }

    fun getReportFromSubmitter(submitterId: Int, submissionId: Int): DbReportDto? {
        return jdbi.inTransaction<DbReportDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(reportDaoClass).getReportFromSubmitter(submitterId, submissionId)
        }
    }

    fun userHasReported(submitterId: Int, submissionId: Int): Boolean {
        return getReportFromSubmitter(submitterId, submissionId) != null
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