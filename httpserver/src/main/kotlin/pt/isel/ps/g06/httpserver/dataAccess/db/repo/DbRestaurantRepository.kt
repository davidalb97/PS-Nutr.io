package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionDto
import pt.isel.ps.g06.httpserver.exception.DatabaseConsistencyException
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain.SUBMITTER
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.exception.InvalidParameterException

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
                val apiSubmitterId = apiDao.getIdByName(restaurantApiType.toString())
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

    /**
     * @throws InvalidInputException If submitterId is invalid.
     */
    private fun validateSubmitterId(handle: Handle, submitterId: Int) {
        //Validate submitter id
        handle.attach(SubmissionSubmitterDao::class.java).getBySubmitterId(submitterId)
                ?: throw InvalidInputException(SUBMITTER, TODO())
    }
}