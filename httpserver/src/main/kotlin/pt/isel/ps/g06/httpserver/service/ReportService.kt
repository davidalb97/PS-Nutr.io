package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.BaseReportResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ReportResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ReportSubmissionDetailMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ReportedSubmissionResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.REPORTABLE_TYPES
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository
import pt.isel.ps.g06.httpserver.model.report.BaseReport
import pt.isel.ps.g06.httpserver.model.report.ReportSubmissionDetail
import pt.isel.ps.g06.httpserver.model.report.ReportedSubmission
import java.util.stream.Stream

@Service
class ReportService(
        val reportDbRepository: ReportDbRepository,
        val submissionDbRepository: SubmissionDbRepository
) {

    val reportDbMapper = ReportResponseMapper()
    val simpleReportDbMapper = ReportedSubmissionResponseMapper()
    val detailedReportMapper = BaseReportResponseMapper()
    val reportSubmissionDetailMapper = ReportSubmissionDetailMapper()

    /**
     * Gets all the reports inside the database
     */
    fun getAllReports(skip: Int?, count: Int?) =
            reportDbRepository.getAll(skip, count).map(reportDbMapper::mapToModel)

    /**
     * Gets all the reports inside the database
     */
    fun getAllReportedSubmissionsBySubmissionType(
            submissionType: String,
            skip: Int?,
            count: Int?
    ): Stream<ReportedSubmission> {
        if (REPORTABLE_TYPES.all { it != submissionType }) {
            throw InvalidInputException(
                    "This type of submission can not be reported and thus there are no reports for this"
            )
        }

        return reportDbRepository.getAllBySubmissionType(submissionType, skip, count).map(simpleReportDbMapper::mapTo)
    }

    /**
     * Gets all the reports for a specific submission
     * @param submissionId - The submission's id
     */
    fun getSubmissionReports(submissionId: Int): Stream<BaseReport> =
            reportDbRepository.getAllFromSubmission(submissionId).map(detailedReportMapper::mapTo)

    fun getReportedSubmissionDetail(submissionId: Int): ReportSubmissionDetail? =
            reportDbRepository.getReportedSubmissionDetail(submissionId)
                    ?.let{ reportSubmissionDetailMapper.mapTo(it, submissionId)}

    fun deleteReport(reportId: Int) {
        reportDbRepository.delete(reportId)
    }
}

