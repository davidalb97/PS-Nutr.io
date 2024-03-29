package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.REPORTABLE_TYPES
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.BaseReportModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.ReportModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.ReportSubmissionDetailMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.ReportedSubmissionModelMapper
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.model.report.BaseReport
import pt.isel.ps.g06.httpserver.model.report.ReportSubmissionDetail
import pt.isel.ps.g06.httpserver.model.report.ReportedSubmission
import pt.isel.ps.g06.httpserver.model.report.SubmissionReport

@Service
class ReportService(
        val reportDbRepository: ReportDbRepository,
        val submissionDbRepository: SubmissionDbRepository
) {

    val reportDbMapper = ReportModelMapper()
    val simpleReportDbMapper = ReportedSubmissionModelMapper()
    val detailedReportMapper = BaseReportModelMapper()
    val reportSubmissionDetailMapper = ReportSubmissionDetailMapper()

    /**
     * Gets all the reports inside the database
     */
    fun getAllReports(skip: Int?, count: Int?): Sequence<SubmissionReport> {
        return reportDbRepository.getAll(skip, count).map(reportDbMapper::mapTo)
    }

    /**
     * Gets all the reports inside the database
     */
    fun getAllReportedSubmissionsBySubmissionType(
            submissionType: String,
            skip: Int?,
            count: Int?
    ): Sequence<ReportedSubmission> {
        if (REPORTABLE_TYPES.all { it != submissionType }) {
            throw InvalidInputException(
                    "This type of submission can not be reported and thus there are no reports for this"
            )
        }

        return reportDbRepository.getAllBySubmissionType(submissionType, skip, count)
                .map(simpleReportDbMapper::mapTo)
    }

    /**
     * Gets all the reports for a specific submission
     * @param submissionId - The submission's id
     */
    fun getSubmissionReports(submissionId: Int): Sequence<BaseReport> =
            reportDbRepository.getAllFromSubmission(submissionId)
                    .map(detailedReportMapper::mapTo)

    fun getReportedSubmissionDetail(submissionId: Int): ReportSubmissionDetail? =
            reportDbRepository.getReportedSubmissionDetail(submissionId)
                    ?.let { reportSubmissionDetailMapper.mapTo(it, submissionId) }

    fun deleteReport(reportId: Int) {
        reportDbRepository.delete(reportId)
    }
}

