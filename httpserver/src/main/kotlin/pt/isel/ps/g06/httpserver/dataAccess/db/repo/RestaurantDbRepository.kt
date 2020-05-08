package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantApiId
import pt.isel.ps.g06.httpserver.springConfig.dto.DbEditableDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val restaurantDaoClass = RestaurantDao::class.java

@Repository
class RestaurantDbRepository(jdbi: Jdbi, val config: DbEditableDto) : BaseDbRepo(jdbi) {

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
            apiId: RestaurantApiId? = null,
            cuisines: List<String> = emptyList(),
            latitude: Float,
            longitude: Float
    ): SubmissionDto {
        return jdbi.inTransaction<SubmissionDto, Exception>(isolationLevel) {

            //Insert Submission
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .insert(RESTAURANT.toString())
            val submissionId = submissionDto.submission_id

            //Insert SubmissionSubmitter
            it.attach(SubmissionSubmitterDao::class.java)
                    .insert(submissionId, submitterId)

            //Insert Restaurant
            it.attach(RestaurantDao::class.java)
                    .insert(submissionId, restaurantName, latitude, longitude)

            //Insert all RestaurantCuisine associations
            it.attach(RestaurantCuisineDao::class.java)
                    .insertAll(cuisines.map { RestaurantCuisineParam(submissionId, it) })

            val contracts = mutableListOf(SubmissionContractType.VOTABLE)
            if (apiId != null) {
                insertApiRestaurant(it, submissionId, apiId)
                contracts.add(SubmissionContractType.API)
            } else contracts.add(SubmissionContractType.REPORTABLE)

            //Insert contracts (VOTABLE,  API if there is an apiId, REPORTABLE if it doesn't)
            it.attach(SubmissionContractDao::class.java)
                    .insertAll(contracts.map { SubmissionContractParam(submissionId, it.toString()) })

            return@inTransaction submissionDto
        }
    }


    fun delete(submitterId: Int, submissionId: Int) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this restaurant
            requireSubmissionSubmitter(submitterId, submissionId, isolationLevel)

            // Check if the submission is a Restaurant
            requireSubmission(submissionId, RESTAURANT, isolationLevel)

            // Check if the submission is modifiable
            requireEditable(submissionId, config.`edit-timeout-minutes`!!, isolationLevel)

            // Delete portions and meals associated to this restaurant
            it.attach(RestaurantMealPortionDao::class.java).deleteAllByRestaurantId(submissionId)

            // Delete all RestaurantCuisine associations
            it.attach(RestaurantCuisineDao::class.java).deleteAllByRestaurantId(submissionId)

            // Delete all restaurant portions
            it.attach(RestaurantMealPortionDao::class.java).deleteAllByRestaurantId(submissionId)

            // Delete all submission contracts
            it.attach(SubmissionContractDao::class.java).deleteAllById(submissionId)

            // Delete all user votes
            it.attach(VoteDao::class.java).deleteAllById(submissionId)

            if (!isFromApi(submissionId)) {
                // Delete all user reports
                it.attach(ReportDao::class.java).deleteAllBySubmissionId(submissionId)
                // Delete api submission relation
                it.attach(ApiSubmissionDao::class.java).deleteById(submissionId)
            }

            // Delete restaurant
            it.attach(restaurantDaoClass).delete(submissionId)

            // Delete submission-submitter association
            it.attach(SubmissionSubmitterDao::class.java).deleteAllBySubmissionId(submissionId)

            // Delete SubmissionContract
            it.attach(SubmissionContractDao::class.java).deleteAllById(submissionId)

            // Delete submission from Submission Table
            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }

    fun update(submitterId: Int, submissionId: Int, name: String, cuisines: List<String>) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this restaurant
            requireSubmissionSubmitter(submitterId, submissionId, isolationLevel)

            // Check if the submission is a Restaurant
            requireSubmission(submissionId, SubmissionType.MEAL, isolationLevel)

            // Check if the submission is modifiable
            requireEditable(submissionId, config.`edit-timeout-minutes`!!, isolationLevel)

            // Update restaurant name
            it.attach(restaurantDaoClass).update(submissionId, name)

            // Update cuisines
            if(cuisines.isNotEmpty()) {
                updateCuisines(it, submissionId, cuisines)
            }
        }
    }

    private fun insertApiRestaurant(handle: Handle, submissionId: Int, apiId: RestaurantApiId) {
        //Get api submitterId, abort if failed
        val apiDao = handle.attach(ApiDao::class.java)
        val apiSubmitterId = apiDao.getByName(apiId.apiType.toString())!!.submitter_id

        //Insert SubmissionSubmitter for the new Api submitter
        handle.attach(SubmissionSubmitterDao::class.java).insert(submissionId, apiSubmitterId)

        //Insert ApiSubmission
        handle.attach(ApiSubmissionDao::class.java)
                .insert(submissionId, apiId.id)
    }

    private fun updateCuisines(handle: Handle, submissionId: Int, cuisines: List<String>) {
        val restaurantCuisineDao = handle.attach(RestaurantCuisineDao::class.java)

        //Get existing cuisines
        val existingCuisines = restaurantCuisineDao.getByRestaurantId(submissionId)
                .toMutableList()

        //Delete cuisines
        val deletedCuisines = existingCuisines
                .filter { !cuisines.contains(it.cuisine_name) }
        if (deletedCuisines.isNotEmpty()) {
            restaurantCuisineDao.deleteAllByRestaurantIdAndCuisine(
                    submissionId,
                    deletedCuisines.map { it.cuisine_name }
            )
        }

        //Insert new cuisines
        existingCuisines.removeAll(deletedCuisines)
        restaurantCuisineDao.insertAll(
                existingCuisines.map {
                    RestaurantCuisineParam(it.restaurant_submission_id, it.cuisine_name)
                }
        )
    }
}