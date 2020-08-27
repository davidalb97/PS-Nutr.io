package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.SUBMISSION
import pt.isel.ps.g06.httpserver.common.SUBMISSION_ID_VALUE
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.RestaurantService
import pt.isel.ps.g06.httpserver.service.SubmissionService

@RestController
class SubmissionController(
        private val submissionService: SubmissionService,
        private val restaurantService: RestaurantService
) {

    @DeleteMapping(SUBMISSION)
    fun deleteSubmission(
            @PathVariable(SUBMISSION_ID_VALUE) submissionId: String,
            user: User
    ): ResponseEntity<Void> {

        val parsedSubmissionId = checkAndParseId(submissionId)

        submissionService.deleteSubmission(parsedSubmissionId, user)

        return ResponseEntity.ok().build()
    }

    /**
     * If the submissionId is from a restaurant then parsing will occur,
     * if not just convert to int and return
     * @param   submissionId    Submission's identifier
     */
    private fun checkAndParseId(submissionId: String): Int {
        if (submissionId.contains("+")) {
            return submissionId.split("+")[1].toInt()
        }

        return submissionId.toInt()
    }
}