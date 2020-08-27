package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.ID_SEPARATOR
import pt.isel.ps.g06.httpserver.common.RestaurantIdentifierBuilder
import pt.isel.ps.g06.httpserver.common.SUBMISSION
import pt.isel.ps.g06.httpserver.common.SUBMISSION_ID_VALUE
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.SubmissionNotFoundException
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.SubmissionService

@RestController
class SubmissionController(
        private val submissionService: SubmissionService,
        private val restaurantIdentifierBuilder: RestaurantIdentifierBuilder
) {

    @DeleteMapping(SUBMISSION)
    fun deleteSubmission(
            @PathVariable(SUBMISSION_ID_VALUE) submissionId: String,
            user: User
    ): ResponseEntity<Void> {

        val parsedSubmissionId = parseId(submissionId)

        submissionService.deleteSubmission(parsedSubmissionId, user)

        return ResponseEntity.ok().build()
    }

    /**
     * If the submissionId is from a restaurant then parsing will occur,
     * if not just convert to int and return
     * @param   identifier    Submission's identifier
     */
    private fun parseId(identifier: String): Int {
        if (identifier.contains(ID_SEPARATOR)) {
            return restaurantIdentifierBuilder
                    .extractIdentifiers(identifier).submissionId ?: throw SubmissionNotFoundException()
        }
        return identifier.toIntOrNull() ?: throw InvalidInputException("The passed identifier is invalid")
    }
}