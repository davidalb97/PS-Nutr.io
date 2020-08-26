package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.model.Report
import pt.isel.ps.g06.httpserver.model.SimplifiedReport
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.ReportService
import pt.isel.ps.g06.httpserver.service.UserService

@RestController
class ReportController(
        private val reportService: ReportService,
        private val userService: UserService
) {

    @GetMapping(REPORTS)
    fun getReports(
            user: User,
            @RequestParam type: SubmissionType?
    ): ResponseEntity<Collection<*>> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        type ?: return ResponseEntity.ok(reportService.getAllReports().toList())

        return ResponseEntity.ok(reportService.getAllReportsBySubmissionType(type.toString()))
    }


    @GetMapping(SUBMISSION_REPORT)
    fun getAllReportsFromSubmission(
            @PathVariable(SUBMISSION_ID_VALUE) submissionId: Int,
            user: User
    ): ResponseEntity<Collection<Report>>  {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        return ResponseEntity.ok(reportService.getSubmissionReports(submissionId).toList())
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