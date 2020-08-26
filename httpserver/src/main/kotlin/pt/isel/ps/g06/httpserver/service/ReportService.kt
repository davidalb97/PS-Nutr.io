package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidInputException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ReportResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.SimpleReportResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.REPORTABLE_TYPES
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository
import pt.isel.ps.g06.httpserver.model.SimplifiedReport

@Service
class ReportService(
        val reportDbRepository: ReportDbRepository,
        val submissionDbRepository: SubmissionDbRepository
) {

    val reportDbMapper = ReportResponseMapper()
    val simpleReportDbMapper = SimpleReportResponseMapper()

    /**
     * Gets all the reports inside the database
     */
    fun getAllReports() = reportDbRepository.getAll().map(reportDbMapper::mapToModel)

    /**
     * Gets all the reports inside the database
     */
    fun getAllReportsBySubmissionType(submissionType: String): Collection<SimplifiedReport> {
        if (REPORTABLE_TYPES.all { it != submissionType }) {
            throw InvalidInputException(
                    "This type of submission can not be reported and thus there are no reports for this"
            )
        }

        return reportDbRepository.getAllBySubmissionType(submissionType).map(simpleReportDbMapper::mapTo)
    }

    /**
     * Gets all the reports for a specific submission
     * @param submissionId - The submission's id
     */
    fun getSubmissionReports(submissionId: Int) =
            reportDbRepository.getAllFromSubmission(submissionId).map(reportDbMapper::mapToModel)

    fun deleteReport(reportId: Int) =
            reportDbRepository.delete(reportId).let(reportDbMapper::mapToModel)
}