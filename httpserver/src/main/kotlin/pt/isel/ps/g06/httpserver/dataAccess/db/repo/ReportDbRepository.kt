package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.InvalidReportSubmissionException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.conflict.DuplicateReportException
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.REPORTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ReportDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.*
import java.util.stream.Stream

private val reportDaoClass = ReportDao::class.java

@Repository
class ReportDbRepository(private val databaseContext: DatabaseContext, private val submissionDbRepository: SubmissionDbRepository) {

    fun getAll(skip: Int?, count: Int?): Stream<DbReportSubmissionDto> {
        return databaseContext.inTransaction {
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

    fun getAllBySubmissionType(submissionType: String, skip: Int?, count: Int?): Stream<DbReportedSubmissionDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(reportDaoClass).getAllReportedSubmissionsByType(submissionType, skip, count)
        }
    }

    fun getAllFromSubmission(submissionId: Int): Stream<DbSubmissionReportDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(reportDaoClass).getAllBySubmission(submissionId)
        }
    }

    fun getReportedSubmissionDetail(submissionId: Int): DbReportSubmissionDetailDto? {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(reportDaoClass).getReportedSubmissionDetail(submissionId)
        }
    }

    fun getReportFromSubmitter(submitterId: Int, submissionId: Int): DbReportDto? {
        return databaseContext.inTransaction {
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
        return databaseContext.inTransaction {
            if (!submissionDbRepository.hasContract(submissionId, REPORTABLE)) {
                throw InvalidReportSubmissionException()
            }

            if (isSubmissionReported(submitterId, submissionId)) {
                throw DuplicateReportException()
            }

            return@inTransaction it
                    .attach(reportDaoClass)
                    .insert(submitterId, submissionId, report)
        }
    }

    fun delete(reportId: Int): DbReportDto {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(reportDaoClass).deleteById(reportId)
        }
    }

    private fun isSubmissionReported(submitterId: Int, submissionId: Int): Boolean {
        return getReportFromSubmitter(submitterId, submissionId) != null
    }
}