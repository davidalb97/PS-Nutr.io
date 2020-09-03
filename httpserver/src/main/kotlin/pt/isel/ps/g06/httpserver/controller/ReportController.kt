package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.SubmissionNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.output.report.SubmissionReportsContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.report.toGenericReportsOutputContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.report.toReportedSubmissionsOutputContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.report.toSubmissionReportsContainerOutput
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.ReportService
import pt.isel.ps.g06.httpserver.service.UserService
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@RestController
class ReportController(
        private val reportService: ReportService,
        private val userService: UserService
) {

    @GetMapping(REPORTS)
    fun getReports(
            user: User,
            @RequestParam type: SubmissionType?,
            @RequestParam @Min(0) skip: Int?,
            @RequestParam @Min(0) @Max(MAX_COUNT) count: Int?
    ): ResponseEntity<*> {

        // Check if the user is a moderator
        userService.ensureModerator(user)
        val body = type?.let {
            toReportedSubmissionsOutputContainer(
                    reportService.getAllReportedSubmissionsBySubmissionType(type.toString(), skip, count)
            )
        } ?: toGenericReportsOutputContainer(reportService.getAllReports(skip, count).toList())

        return ResponseEntity.ok(body)
    }


    @GetMapping(SUBMISSION_REPORT)
    fun getAllReportsFromSubmission(
            @PathVariable(SUBMISSION_ID_VALUE) submissionId: Int,
            user: User
    ): ResponseEntity<SubmissionReportsContainerOutput> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        val reportedSubmissionDetail = reportService.getReportedSubmissionDetail(submissionId)
                ?: throw SubmissionNotFoundException()

        val submissionReports = reportService.getSubmissionReports(submissionId)

        return ResponseEntity.ok(
                toSubmissionReportsContainerOutput(
                        reportedSubmissionDetail,
                        submissionReports
                )
        )
    }

    @DeleteMapping(REPORT)
    fun deleteReport(
            @PathVariable(REPORT_ID_VALUE) submissionId: Int,
            user: User
    ): ResponseEntity<Void> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        reportService.deleteReport(submissionId)

        return ResponseEntity.ok().build()
    }
}