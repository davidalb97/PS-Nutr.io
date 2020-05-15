package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.*
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantApiId
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.springConfig.dto.DbEditableDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val restaurantDaoClass = RestaurantDao::class.java

@Repository
class RestaurantDbRepository(jdbi: Jdbi, val config: DbEditableDto) : BaseDbRepo(jdbi) {

    fun getById(id: Int): DbRestaurantDto? {
        return jdbi.inTransaction<DbRestaurantDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(restaurantDaoClass).getById(id)
        }
    }

    fun getRestaurantCuisines(id: Int): Collection<DbCuisineDto> {
        return jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(CuisineDao::class.java).getByRestaurantId(id)
        }
    }

    fun getAllByCoordinates(latitude: Float, longitude: Float, radius: Int): Collection<DbRestaurantDto> {
        return jdbi.inTransaction<List<DbRestaurantDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(restaurantDaoClass).getByCoordinates(latitude, longitude, radius)
        }
    }

    fun insert(
            submitterId: Int,
            restaurantName: String,
            apiId: RestaurantApiId? = null,
            cuisineNames: Collection<String> = emptyList(),
            latitude: Float,
            longitude: Float
    ): DbSubmissionDto {
        return jdbi.inTransaction<DbSubmissionDto, Exception>(isolationLevel) {

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
            val cuisineIds = it.attach(CuisineDao::class.java)
                    .getAllByNames(cuisineNames)
                    .map { it.cuisine_id }
            it.attach(RestaurantCuisineDao::class.java)
                    .insertAll(cuisineIds.map { DbRestaurantCuisineDto(submissionId, it) })

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

    /**
     * @throws InvalidInputException On invalid submission ownership, invalid submission type,
     *                               submission change timed out.
     *                               (Annotation required for testing purposes)
     */
    @Throws(InvalidInputException::class)
    fun delete(submitterId: Int, submissionId: Int) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this restaurant
            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

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

    /**
     * @throws InvalidInputException On invalid submission ownership, invalid submission type,
     *                               submission change timed out.
     *                               (Annotation required for testing purposes)
     */
    @Throws(InvalidInputException::class)
    fun update(submitterId: Int, submissionId: Int, name: String, cuisines: List<String>) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this restaurant
            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

            // Check if the submission is a Restaurant
            requireSubmission(submissionId, SubmissionType.MEAL, isolationLevel)

            // Check if the submission is modifiable
            requireEditable(submissionId, config.`edit-timeout-minutes`!!, isolationLevel)

            // Update restaurant name
            it.attach(restaurantDaoClass).update(submissionId, name)

            // Update cuisines
            if (cuisines.isNotEmpty()) {
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

    private fun updateCuisines(handle: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        val cuisineDtos = handle.attach(CuisineDao::class.java)
                .getAllByNames(cuisineNames)
        val restaurantCuisineDao = handle.attach(RestaurantCuisineDao::class.java)

        //Get existing cuisines
        val existingMealCuisineIds = restaurantCuisineDao.getByRestaurantId(submissionId)
                .map { it.cuisine_id }
                .toMutableList()

        //Delete cuisines
        val deletedCuisineIds = existingMealCuisineIds
                .filter { existing -> cuisineDtos.none { it.cuisine_id == existing } }
        if (deletedCuisineIds.isNotEmpty()) {
            restaurantCuisineDao.deleteAllByRestaurantIdAndCuisineIds(submissionId, deletedCuisineIds)
        }

        //Insert new cuisines
        existingMealCuisineIds.removeAll(deletedCuisineIds)
        val newCuisineIds = cuisineDtos.filter { cuisine ->
            existingMealCuisineIds.none { it == cuisine.cuisine_id }
        }.map { it.cuisine_id }

        if (newCuisineIds.isNotEmpty()) {
            restaurantCuisineDao.insertAll(newCuisineIds.map { DbRestaurantCuisineDto(submissionId, it) })
        }
    }
}