package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.MOD_USER
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.VoteDbRepository
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.SubmissionNotVotableException
import pt.isel.ps.g06.httpserver.exception.problemJson.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.exception.problemJson.notFound.SubmissionNotFoundException
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant

/**
 * Handles actions that are common to any Submission (like Restaurant, Meal, RestaurantMeal),
 * such as submission deletion and managing votes.
 */
@Service
class SubmissionService(
        private val submissionDbRepository: SubmissionDbRepository,
        private val voteDbRepository: VoteDbRepository
) {

    /**
     * Deletes given submission and all of it's attributes (votes, reports, etc),
     * if given user was the creator.
     */
    fun deleteSubmission(submissionId: Int, user: User) {
        if (user.userRole != MOD_USER) {
            val submitter = submissionDbRepository
                    .getSubmitterForSubmissionId(submissionId)
                    ?: throw SubmissionNotFoundException()

            if (submitter.submitter_id != user.identifier) {
                throw NotSubmissionOwnerException()
            }
        }

        submissionDbRepository.deleteSubmissionById(submissionId)
    }

    fun alterRestaurantVote(restaurant: Restaurant, submitterId: Int, voteState: VoteState) {
        if (!restaurant.isPresentInDatabase() || !restaurant.isUserRestaurant()) {
            throw SubmissionNotVotableException("Only restaurants created by users can have votes!")
        }

        //Is present in database check makes sure submissionId is not null
        setSubmissionVote(restaurant.identifier.value.submissionId!!, submitterId, voteState)
    }

    fun alterRestaurantMealVote(restaurantMealId: Int, submitterId: Int, voteState: VoteState) {

        setSubmissionVote(
                submissionId = restaurantMealId,
                submitterId = submitterId,
                voteState = voteState
        )
    }

    private fun setSubmissionVote(submissionId: Int, submitterId: Int, voteState: VoteState) {
        voteDbRepository.setVote(
                submissionId = submissionId,
                voterId = submitterId,
                newVote = voteState
        )
    }
}