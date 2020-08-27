package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.SUBMISSION
import pt.isel.ps.g06.httpserver.common.SUBMISSION_ID_VALUE
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.SubmissionService

@RestController
class SubmissionController(
        private val submissionService: SubmissionService
) {

    @DeleteMapping(SUBMISSION)
    fun deleteSubmission(
            @PathVariable(SUBMISSION_ID_VALUE) submissionId: Int,
            user: User
    ): ResponseEntity<Void> {

        submissionService.deleteSubmission(submissionId, user)

        return ResponseEntity.ok().build()
    }
}