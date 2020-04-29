package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.hibernate.validator.internal.engine.ValidatorImpl
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionDto
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain.SUBMITTER
import pt.isel.ps.g06.httpserver.exception.InvalidInputException

@Repository
class RestaurantDbRepository(private val jdbi: Jdbi) {

    private val serializable = TransactionIsolationLevel.SERIALIZABLE
    private val restaurantDao = RestaurantDao::class.java

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
    ): SubmissionDto {
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

                //Insert SubmissionSubmitter for the new Api submitter
                submissionSubmitterDao.insert(submissionDto.submission_id, apiSubmitterId)

                //Insert ApiSubmission
                val apiSubmissionDao = it.attach(ApiSubmissionDao::class.java)
                apiSubmissionDao.insert(submissionDto.submission_id, apiId, restaurantApiType.toString())
            }

            return@inTransaction submissionDto
        }
    }


    fun deleteRestaurant(
            submitterId: Int,
            submission_id: Int
    ) {
        return inTransaction(jdbi, serializable) {
            validateSubmitterId(it, submitterId)

            // Check if the restaurant with this submission_id exists
            it.attach(RestaurantDao::class.java)
                    .getById(submission_id)
                    ?: return@inTransaction

            val restaurantMealPortions = it.attach(RestaurantMealPortionDao::class.java)
                    .getAllByRestaurantId(submission_id)

            // Delete portions and meals associated to this restaurant
            if (restaurantMealPortions.isNotEmpty()) {
                it.attach(RestaurantMealPortionDao::class.java).deleteFromRestaurant(submission_id)
            }

            // Delete all cuisines from this restaurant
            it.attach(RestaurantCuisineDao::class.java).deleteAllByRestaurantId(submission_id)

            // Delete restaurant
            it.attach(RestaurantDao::class.java).delete(submission_id)

            // Delete submission-submitter association
            it.attach(SubmissionSubmitterDao::class.java).delete(submission_id)

            // Delete submission from Submission Table
            it.attach(SubmissionDao::class.java).delete(submission_id)
        }
    }
}