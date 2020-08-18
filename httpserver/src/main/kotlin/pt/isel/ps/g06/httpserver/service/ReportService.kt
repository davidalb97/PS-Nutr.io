package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.notFound.SubmissionNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ReportResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.ReportInput

@Service
class ReportService(
        val reportDbRepository: ReportDbRepository,
        val submissionDbRepository: SubmissionDbRepository
) {

    val reportDbMapper = ReportResponseMapper()

    /**
     * Gets all the reports inside the database
     */
    fun getAllReports() = reportDbRepository.getAll().map(reportDbMapper::mapToModel)

    /**
     * Gets all the reports for a specific submission
     * @param submissionId - The submission's id
     */
    fun getSubmissionReports(submissionId: Int) =
            reportDbRepository.getAllFromSubmission(submissionId).map(reportDbMapper::mapToModel)

    /**
     * Inserts a report for a specific submission
     * @param reportInput - The user's report
     * @param reportSubmitter - The reporter's submitter id
     */
    fun insertReport(reportInput: ReportInput, reportSubmitter: Int) {

        val dbSubmissionDto = submissionDbRepository.getSubmissionById(reportInput.submissionId)
                ?: throw SubmissionNotFoundException()

        reportDbRepository.insert(
                submitterId = reportSubmitter,
                submissionId = dbSubmissionDto.submission_id,
                report = reportInput.description
        )
    }

}