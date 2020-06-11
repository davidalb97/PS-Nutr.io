package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbPublicDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantItemDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantMealItemDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantApiId
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.springConfig.dto.DbEditableDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val restaurantDaoClass = RestaurantDao::class.java

@Repository
class RestaurantDbRepository(jdbi: Jdbi, val config: DbEditableDto) : BaseDbRepo(jdbi) {

    private val contracts = listOf(REPORTABLE, FAVORABLE, API, VOTABLE)

    fun getInfoById(id: Int, userId: Int, restaurantMeals: Collection<DbRestaurantMealItemDto>): DbRestaurantInfoDto? {
        return jdbi.inTransaction<DbRestaurantInfoDto, Exception>(isolationLevel) {
            val restaurantDto = it.attach(RestaurantDao::class.java).getById(id)
                    ?: return@inTransaction null
            val apiId = it.attach(ApiSubmissionDao::class.java).getBySubmissionId(id)?.apiId

            //TODO must only be one submitter per submission
            val submitterId = it.attach(SubmissionSubmitterDao::class.java)
                    .getAllBySubmissionId(id).first().submitter_id
            val votesDtos = it.attach(VotableDao::class.java).getById(restaurantDto.submission_id)
                    ?: DbVotesDto(0, 0)
            val userVote = it.attach(UserVoteDao::class.java).getVoteByIds(restaurantDto.submission_id, userId)
            val isFavorite = it.attach(FavoriteDao::class.java).getByIds(restaurantDto.submission_id, userId)
                    ?.let { true } ?: false
            return@inTransaction DbRestaurantInfoDto(
                    restaurantItem = DbRestaurantItemDto(
                            restaurant = restaurantDto,
                            submitterId = submitterId,
                            apiId = apiId,
                            image = null,
                            isFavorite = isFavorite,
                            public = DbPublicDto(
                                    votes = votesDtos,
                                    userVote = userVote
                            )
                    ),
                    cuisines = getRestaurantCuisines(id).map { it.cuisine_name },
                    restaurantMeals = restaurantMeals
            )
        }
    }

    fun getRestaurantCuisines(id: Int): Collection<DbCuisineDto> {
        return jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(CuisineDao::class.java).getByRestaurantId(id)
        }
    }

    fun getAllByCoordinates(latitude: Float, longitude: Float, radius: Int, userId: Int): Collection<DbRestaurantItemDto> {
        return jdbi.inTransaction<Collection<DbRestaurantItemDto>, Exception>(isolationLevel) {
            val restaurantDtos = it.attach(RestaurantDao::class.java)
                    .getByCoordinates(latitude, longitude, radius)
            //return@inTransaction
            return@inTransaction restaurantDtos.map { restaurantDto ->
                val apiId = it.attach(ApiSubmissionDao::class.java)
                        .getBySubmissionId(restaurantDto.submission_id)?.apiId
                //TODO must only be one submitter per submission
                val submitterId = it.attach(SubmissionSubmitterDao::class.java)
                        .getAllBySubmissionId(restaurantDto.submission_id).first().submitter_id
                val votesDtos = it.attach(VotableDao::class.java).getById(restaurantDto.submission_id)
                        ?: DbVotesDto(0, 0)
                val userVote = it.attach(UserVoteDao::class.java).getVoteByIds(restaurantDto.submission_id, userId)
                val isFavorite = it.attach(FavoriteDao::class.java).getByIds(restaurantDto.submission_id, userId)
                        ?.let { true } ?: false
                DbRestaurantItemDto(
                        restaurant = restaurantDto,
                        submitterId = submitterId,
                        apiId = apiId,
                        image = null,
                        isFavorite = isFavorite,
                        public = DbPublicDto(
                                votes = votesDtos,
                                userVote = userVote
                        )
                )
            }
        }
    }

    /**
     * @throws InvalidInputException On invalid cuisines passed.
     *                               (Annotation required for testing purposes)
     */
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
            insertRestaurantCuisines(it, submissionId, cuisineNames)

            val contracts = this.contracts.toMutableList()
            if (apiId != null) {
                insertApiRestaurant(it, submissionId, apiId)
                contracts.remove(VOTABLE)
            } else contracts.remove(API)

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

            // Check if the submission is modifiable
            requireEditable(submissionId, config.`edit-timeout-minutes`!!, isolationLevel)

            // Delete RestaurantMeal portions
            deleteRestaurantPortions(it, submissionId)

            // Delete RestaurantMeals from this restaurant
            it.attach(RestaurantMealDao::class.java).deleteAllByRestaurantId(submissionId)

            // Delete all RestaurantCuisine associations
            it.attach(RestaurantCuisineDao::class.java).deleteAllByRestaurantId(submissionId)

            // Delete restaurant
            it.attach(restaurantDaoClass).delete(submissionId)

            // Removes submission, submitter association, contracts & it's tables
            super.removeSubmission(submissionId, submitterId, RESTAURANT, contracts, isolationLevel)
        }
    }

    /**
     * @throws InvalidInputException On invalid submission ownership, invalid submission type,
     *                               submission change timed out or invalid cuisines were passed.
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

    private fun insertRestaurantCuisines(it: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        val cuisineIds = getCuisinesByNames(cuisineNames, isolationLevel)
                .map { it.submission_id }
        it.attach(RestaurantCuisineDao::class.java)
                .insertAll(cuisineIds.map { DbRestaurantCuisineDto(submissionId, it) })
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
        val cuisineDtos = getCuisinesByNames(cuisineNames, isolationLevel)
        val restaurantCuisineDao = handle.attach(RestaurantCuisineDao::class.java)

        //Get existing cuisines
        val existingMealCuisineIds = restaurantCuisineDao.getByRestaurantId(submissionId)
                .map { it.cuisine_submission_id }
                .toMutableList()

        //Delete cuisines
        val deletedCuisineIds = existingMealCuisineIds
                .filter { existing -> cuisineDtos.none { it.submission_id == existing } }
        if (deletedCuisineIds.isNotEmpty()) {
            restaurantCuisineDao.deleteAllByRestaurantIdAndCuisineIds(submissionId, deletedCuisineIds)
        }

        //Insert new cuisines
        existingMealCuisineIds.removeAll(deletedCuisineIds)
        val newCuisineIds = cuisineDtos.filter { cuisine ->
            existingMealCuisineIds.none { it == cuisine.submission_id }
        }.map { it.submission_id }

        if (newCuisineIds.isNotEmpty()) {
            restaurantCuisineDao.insertAll(newCuisineIds.map { DbRestaurantCuisineDto(submissionId, it) })
        }
    }

    private fun deleteRestaurantPortions(handle: Handle, submissionId: Int) {
        //Get all restaurant meal ids
        val restaurantMealIds = handle.attach(RestaurantMealDao::class.java)
                .getAllByRestaurantId(submissionId)
                .map { it.submission_id }
        //Delete all portions with the restaurant meal ids
        handle.attach(PortionDao::class.java).deleteAllByRestaurantMealIds(restaurantMealIds)
    }
}