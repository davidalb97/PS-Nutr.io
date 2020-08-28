package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.output.report.DetailedReportContainer
import pt.isel.ps.g06.httpserver.dataAccess.output.report.toDetailedReportContainer
import pt.isel.ps.g06.httpserver.model.DetailedReport
import pt.isel.ps.g06.httpserver.model.Report
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
    ): ResponseEntity<Collection<*>> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        type ?: return ResponseEntity.ok(reportService.getAllReports(skip, count).toList())

        return ResponseEntity.ok(reportService.getAllReportsBySubmissionType(type.toString(), skip, count))
    }


    @GetMapping(SUBMISSION_REPORT)
    fun getAllReportsFromSubmission(
            @PathVariable(SUBMISSION_ID_VALUE) submissionId: Int,
            user: User
    ): ResponseEntity<DetailedReportContainer>  {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        val reportedSubmissionName = reportService.getReportedSubmissionName(submissionId)
        val submissionReports = reportService.getSubmissionReports(submissionId)

        return ResponseEntity.ok(
                toDetailedReportContainer(
                        reportedSubmissionName?.restaurantName,
                        submissionReports)
        )
    }

    @DeleteMapping(REPORT)
    fun deleteReport(
            @PathVariable(REPORT_ID_VALUE) submissionId: Int,
            user: User
    ): ResponseEntity<Void>  {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        reportService.deleteReport(submissionId)

        return ResponseEntity.ok().build()
    }
}