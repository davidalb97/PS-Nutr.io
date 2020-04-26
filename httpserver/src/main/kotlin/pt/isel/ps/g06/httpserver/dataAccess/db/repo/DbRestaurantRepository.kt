package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ReportDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.VotableDto
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain.SUBMITTER
import pt.isel.ps.g06.httpserver.exception.InvalidInputException

@Repository
class DbRestaurantRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val restaurantDao = RestaurantDao::class.java

    fun getRestaurantById(id: Int): RestaurantDto? {
        return inTransaction(jdbi, serializable) {
            return@inTransaction it.attach(restaurantDao).getById(id)
        }
    }

    fun getRestaurantsByCoordinates(latitude: Float, longitude: Float, radius: Int): List<RestaurantDto>? {
        return inTransaction(jdbi, serializable) {
            it.attach(restaurantDao).getByCoordinates(latitude, longitude, radius)
        }
    }

    /**
     * @return Inserted project or null if insert failed.
     * @throws InvalidInputException If submitterId is invalid.
     */
    fun insertRestaurant(
            submitterId: Int,
            restaurantName: String,
            apiId: Int? = null,
            cuisines: List<String> = emptyList(),
            latitude: Float,
            longitude: Float,
            restaurantApiType: RestaurantApiType? = null
    ): SubmissionDto? {
        return inTransaction(jdbi, serializable) {

            validateSubmitterId(it, submitterId)

            //Insert Submission
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .insert(SubmissionType.Restaurant.name)

            //Insert SubmissionSubmitter
            val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)
            submissionSubmitterDao.insert(submissionDto.submission_id, submitterId)

            //Insert Restaurant
            it.attach(RestaurantDao::class.java)
                    .insert(submissionDto.submission_id, restaurantName, latitude, longitude)

            //Insert Cuisines, ignore failed inserts
            val cuisineDao = it.attach(CuisineDao::class.java)
            cuisineDao.insertAll(cuisines.map(::CuisineParam))

            if (restaurantApiType != null && apiId != null) {

                //Get api submitterId, abort if failed
                val apiDao = it.attach(ApiDao::class.java)
                val apiSubmitterId = apiDao.getByName(restaurantApiType.toString())!!.submitter_id
                        ?: it.rollback().let { return@inTransaction null }

                //Insert SubmissionSubmitter for the new Api submitter
                submissionSubmitterDao.insert(submissionDto.submission_id, apiSubmitterId)

                //Insert ApiSubmission
                val apiSubmissionDao = it.attach(ApiSubmissionDao::class.java)
                apiSubmissionDao.insert(submissionDto.submission_id, apiId, restaurantApiType.toString())
            }

            submissionDto
        }
    }


    fun deleteRestaurant(
            submitterId: Int,
            submission_id: Int
    ): Boolean {
        return inTransaction(jdbi, serializable) {
            validateSubmitterId(it, submitterId)

            // Check if the restaurant with this submission_id exists
            // TODO - Should have an inner join inside RestaurantDao
            val restaurantDto = it.attach(RestaurantDao::class.java)
                    .getById(submission_id)

            if (restaurantDto != null) {
                val deletedFromSubmission = it.attach(SubmissionDao::class.java)
                        .delete(submission_id)

                if (deletedFromSubmission) {
                    // TODO - Only receives cuisineName ?
                    /*val deletedFromRestaurantCuisines = it.attach(RestaurantCuisineDao::class.java)
                            .delete(submission_id, )*/
                    // Delete submission from Restaurant Table
                    val deletedFromRestaurant = it.attach(RestaurantDao::class.java)
                            .delete(submission_id)

                    if (deletedFromRestaurant)
                        return@inTransaction true
                }

            }


            false
        }
    }

    fun addReport(
            submitterId: Int,
            submission_id: Int,
            report: String
    ): ReportDto? {
        return inTransaction(jdbi, serializable) {
            validateSubmitterId(it, submitterId)

            // TODO - Change to restaurant with inner join
            // Check if the submission exists
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(submission_id)

            if (submissionDto != null) {
                // Submit a report to that Submission
                return@inTransaction it.attach(ReportDao::class.java)
                        .insert(submitterId, submission_id, report)
            }

            null
        }
    }

    fun addVote(
            submitterId: Int,
            submission_id: Int,
            vote: Boolean
    ): VotableDto? {
        return inTransaction(jdbi, serializable) {
            validateSubmitterId(it, submitterId)

            // TODO - Change to restaurant with inner join
            // Check if the submission exists
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (submissionDto != null && !hasVoted) {
                // Submit a report to that Submission
                return@inTransaction it.attach(VotableDao::class.java)
                        .insert(submission_id, submitterId, vote)
            }

            null
        }
    }

    fun removeVote(
            submitterId: Int,
            submission_id: Int
    ): VotableDto? {
        return inTransaction(jdbi, serializable) {
            validateSubmitterId(it, submitterId)

            // TODO - Change to restaurant with inner join
            // Check if the submission exists
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (submissionDto != null && !hasVoted) {
                // Submit a report to that Submission
                return@inTransaction it.attach(VotableDao::class.java)
                        .delete(submission_id, submitterId)
            }

            null
        }
    }

    fun updateVote(
            submitterId: Int,
            submission_id: Int,
            vote: Boolean
    ): VotableDto? {
        return inTransaction(jdbi, serializable) {
            validateSubmitterId(it, submitterId)

            // TODO - Change to restaurant with inner join
            // Check if the submission exists
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(submission_id)

            // Check if this submitter already voted this submission
            val hasVoted = it.attach(VotableDao::class.java)
                    .getVoteFromSubmitter(submission_id, submitterId)

            if (submissionDto != null && !hasVoted) {
                // Submit a report to that Submission
                return@inTransaction it.attach(VotableDao::class.java)
                        .update(submission_id, submitterId, vote)
            }

            null
        }
    }

    /**
     * @throws InvalidInputException If submitterId is invalid.
     */
    private fun validateSubmitterId(handle: Handle, submitterId: Int) {
        //Validate submitter id
        handle.attach(SubmissionSubmitterDao::class.java).getBySubmitterId(submitterId)
                ?: throw InvalidInputException(SUBMITTER, TODO())
    }
}