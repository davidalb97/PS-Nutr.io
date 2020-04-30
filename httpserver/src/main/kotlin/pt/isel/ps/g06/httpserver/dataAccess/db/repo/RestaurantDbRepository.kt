package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val restaurantDaoClass = RestaurantDao::class.java

@Repository
class RestaurantDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi, isolationLevel) {

    fun getById(id: Int): RestaurantDto? {
        return jdbi.inTransaction<RestaurantDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(restaurantDaoClass).getById(id)
        }
    }

    fun getAllByCoordinates(latitude: Float, longitude: Float, radius: Int): List<RestaurantDto> {
        return jdbi.inTransaction<List<RestaurantDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(restaurantDaoClass).getByCoordinates(latitude, longitude, radius)
        }
    }

    fun insert(
            submitterId: Int,
            restaurantName: String,
            apiId: Int? = null,
            cuisines: List<String> = emptyList(),
            latitude: Float,
            longitude: Float,
            restaurantApiType: RestaurantApiType? = null
    ): SubmissionDto {
        return jdbi.inTransaction<SubmissionDto, Exception>(isolationLevel) {

            //Insert Submission
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .insert(RESTAURANT.name)
            val submissionId = submissionDto.submission_id

            //Insert SubmissionSubmitter
            val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)
            submissionSubmitterDao.insert(submissionId, submitterId)

            //Insert Restaurant
            it.attach(RestaurantDao::class.java)
                    .insert(submissionId, restaurantName, latitude, longitude)

            //Insert Cuisines, ignore failed inserts
            val cuisineDao = it.attach(CuisineDao::class.java)
            cuisineDao.insertAll(cuisines.map(::CuisineParam))

            if (restaurantApiType != null && apiId != null) {

                //Get api submitterId, abort if failed
                val apiDao = it.attach(ApiDao::class.java)
                val apiSubmitterId = apiDao.getByName(restaurantApiType.toString())!!.submitter_id

                //Insert SubmissionSubmitter for the new Api submitter
                submissionSubmitterDao.insert(submissionId, apiSubmitterId)

                //Insert ApiSubmission
                it.attach(ApiSubmissionDao::class.java)
                        .insert(submissionId, apiId, restaurantApiType.toString())
            }

            return@inTransaction submissionDto
        }
    }


    fun delete(
            submitterId: Int,
            submissionId: Int
    ) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this restaurant
            requireSubmissionSubmitter(submitterId, submissionId)

            // Check if the submission is a Restaurant
            requireSubmission(submissionId, RESTAURANT)

            val restaurantMealPortions = it.attach(RestaurantMealPortionDao::class.java)
                    .getAllByRestaurantId(submissionId)

            // Delete portions and meals associated to this restaurant
            if (restaurantMealPortions.isNotEmpty()) {
                it.attach(RestaurantMealPortionDao::class.java).deleteFromRestaurant(submissionId)
            }

            // Delete all cuisines from this restaurant
            it.attach(RestaurantCuisineDao::class.java).deleteAllByRestaurantId(submissionId)

            // Delete restaurant
            it.attach(restaurantDaoClass).delete(submissionId)

            // Delete submission-submitter association
            it.attach(SubmissionSubmitterDao::class.java).delete(submissionId)

            // Delete submission from Submission Table
            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }
}