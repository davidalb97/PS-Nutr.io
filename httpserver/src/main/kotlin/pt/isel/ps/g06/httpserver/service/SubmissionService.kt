package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.NotYetVotedException
import pt.isel.ps.g06.httpserver.common.exception.clientError.SubmissionNotVotableException
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.SubmissionNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.VoteDbRepository
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
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
    fun deleteSubmission(submissionId: Int, userId: Int) {
        val submitter = submissionDbRepository
                .getSubmitterForSubmissionId(submissionId)
                ?: throw SubmissionNotFoundException()

        if (submitter.submitter_id != userId) {
            throw NotSubmissionOwnerException()
        }

        submissionDbRepository.deleteSubmissionById(submissionId)
    }

    fun alterRestaurantVote(restaurant: Restaurant, submitterId: Int, vote: Boolean) {
        if (!restaurant.isPresentInDatabase() || !restaurant.isUserRestaurant()) {
            throw SubmissionNotVotableException("Only restaurants created by users can have votes!")
        }

        //Is present in database check makes sure submissionId is not null
        alterSubmissionVote(restaurant.identifier.value.submissionId!!, submitterId, vote)
    }

    fun alterRestaurantMealVote(restaurantMeal: RestaurantMeal, submitterId: Int, vote: Boolean) {
        val mealRestaurantInfo = (restaurantMeal
                .getRestaurantMealInfo()
                ?: throw SubmissionNotVotableException("Only restaurant meals created by users can have votes!"))

        alterSubmissionVote(
                submissionId = mealRestaurantInfo.restaurantMealIdentifier,
                submitterId = submitterId,
                vote = vote
        )
    }

    fun deleteRestaurantVote(restaurant: Restaurant, submitterId: Int) {
        if (!restaurant.isUserRestaurant()) {
            throw SubmissionNotVotableException("Only restaurants created by users can have votes!")
        }

        if (restaurant.userVote(submitterId) == VoteState.NOT_VOTED) {
            throw NotYetVotedException("You have not yet voted for this restaurant!")
        }

        //All user meals need to be inserted in database, so submissionId is never null
        deleteSubmissionVote(restaurant.identifier.value.submissionId!!, submitterId)
    }

    fun deleteRestaurantMealVote(restaurantMeal: RestaurantMeal, submitterId: Int) {
        val mealRestaurantInfo = restaurantMeal
                .getRestaurantMealInfo()
                ?: throw SubmissionNotVotableException("Only restaurant meals created by users can have votes!")

        if (mealRestaurantInfo.userVote(submitterId) == VoteState.NOT_VOTED) {
            throw NotYetVotedException("You have not yet voted for this Restaurant Meal!")
        }

        deleteSubmissionVote(mealRestaurantInfo.restaurantMealIdentifier, submitterId)
    }

    private fun alterSubmissionVote(submissionId: Int, submitterId: Int, vote: Boolean) {
        voteDbRepository.insertOrUpdate(
                submissionId = submissionId,
                voterId = submitterId,
                vote = vote
        )
    }

    private fun deleteSubmissionVote(submissionId: Int, submitterId: Int) {
        voteDbRepository.delete(
                submitterId = submitterId,
                submissionId = submissionId
        )
    }
}