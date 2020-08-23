package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.common.exception.authorization.NotAuthorizedException
import pt.isel.ps.g06.httpserver.dataAccess.input.ReportInput
import pt.isel.ps.g06.httpserver.model.Report
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.ReportService
import pt.isel.ps.g06.httpserver.service.UserService

@RestController
class ReportController(
        private val authenticationService: AuthenticationService,
        private val reportService: ReportService,
        private val userService: UserService
) {

    // TODO: Argument resolver for mod authorization(?)

    @GetMapping(REPORTS)
    fun getAllReports(
            @RequestHeader(AUTH_HEADER) jwt: String
    ): ResponseEntity<List<Report>> {
        val requester = authenticationService.getEmailFromJwt(jwt).let(userService::getUserFromEmail)

        if (requester.userRole != MOD_USER) {
            throw NotAuthorizedException()
        }

        return ResponseEntity.ok(reportService.getAllReports().toList())
    }

    @GetMapping(SUBMISSION_REPORTS)
    fun getAllReportsFromSubmission(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(SUBMISSION_ID_VALUE) submissionId: Int
    ): ResponseEntity<List<Report>> {
        val requester = authenticationService.getEmailFromJwt(jwt).let(userService::getUserFromEmail)

        if (requester.userRole != MOD_USER) {
            throw NotAuthorizedException()
        }

        return ResponseEntity.ok(reportService.getSubmissionReports(submissionId).toList())
    }

    @PostMapping(REPORTS)
    fun postReport(
            submitter: Submitter,
            @RequestBody reportInput: ReportInput
    ) {
        reportService.insertReport(reportInput, submitter.identifier)
    }
}