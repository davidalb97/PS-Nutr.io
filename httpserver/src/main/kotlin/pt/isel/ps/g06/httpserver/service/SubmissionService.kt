package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.SubmissionNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository

@Service
class SubmissionService(private val submissionDbRepository: SubmissionDbRepository) {

    /**
     * Deletes given submission and all of it's attributes (votes, reports, etc),
     * if given user was the creator.
     */
    fun deleteSubmission(submissionId: Int, userId: Int) {
        val submitter = submissionDbRepository
                .getSubmitterForSubmissionId(submissionId)
                ?: throw SubmissionNotFoundException()

        if (submitter.submitter_id != userId) {
            throw NotSubmissionOwnerException()
        }

        submissionDbRepository.deleteSubmission(submissionId)
    }
}