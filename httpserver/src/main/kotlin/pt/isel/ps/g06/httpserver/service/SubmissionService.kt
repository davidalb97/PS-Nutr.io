package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.SubmissionNotVotableException
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.SubmissionNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.VoteDbRepository
import pt.isel.ps.g06.httpserver.model.MealRestaurantInfo
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.VoteState

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
    fun deleteSubmission(submissionId: Int, userId: Int, isMod: Boolean) {
        if (!isMod) {
            val submitter = submissionDbRepository
                    .getSubmitterForSubmissionId(submissionId)
                    ?: throw SubmissionNotFoundException()

            if (submitter.submitter_id != userId) {
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

    fun alterRestaurantMealVote(mealRestaurantInfo: MealRestaurantInfo, submitterId: Int, voteState: VoteState) {

        setSubmissionVote(
                submissionId = mealRestaurantInfo.restaurantMealIdentifier,
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