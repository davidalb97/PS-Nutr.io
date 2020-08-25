package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.model.Report
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.ReportService
import pt.isel.ps.g06.httpserver.service.UserService

@RestController
class ReportController(
        private val authenticationService: AuthenticationService,
        private val reportService: ReportService,
        private val userService: UserService
) {

    @GetMapping(REPORTS)
    fun getAllReports(user: User): ResponseEntity<List<Report>> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        return ResponseEntity.ok(reportService.getAllReports().toList())
    }


    @GetMapping(SUBMISSION_REPORT)
    fun getAllReportsFromSubmission(
            @PathVariable(SUBMISSION_ID_VALUE) submissionId: Int,
            user: User
    ): ResponseEntity<List<Report>>  {

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