package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.exception.InvalidInputException

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val restaurantDaoClass = RestaurantDao::class.java

@Repository
class RestaurantDbRepository(jdbi: Jdbi) : SubmissionDbRepository(jdbi) {
    fun getAllByCoordinates(latitude: Float, longitude: Float, radius: Int): Sequence<DbRestaurantDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbRestaurantDto>, Exception>(isolationLevel) {
                return@inTransaction it.attach(RestaurantDao::class.java)
                        .getByCoordinates(latitude, longitude, radius)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getById(restaurantId: Int): DbRestaurantDto? {
        return jdbi.inTransaction<DbRestaurantDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(RestaurantDao::class.java)
                    .getBySubmissionId(restaurantId)
        }
    }

    fun getApiRestaurant(apiSubmitterId: Int, apiId: String): DbRestaurantDto? {
        return jdbi.inTransaction<DbRestaurantDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(restaurantDaoClass).getApiRestaurant(apiSubmitterId, apiId)
        }
    }


    /**
     * @throws InvalidInputException On invalid cuisines passed.
     *                               (Annotation required for testing purposes)
     */
    fun insert(
            submitterId: Int,
            restaurantName: String,
            apiId: String? = null,
            cuisineNames: Collection<String> = emptyList(),
            latitude: Float,
            longitude: Float,
            ownerId: Int
    ): DbRestaurantDto {
        return jdbi.inTransaction<DbRestaurantDto, Exception>(isolationLevel) {
            //Insert Submission
            val submissionId = it
                    .attach(SubmissionDao::class.java)
                    .insert(RESTAURANT.toString())
                    .submission_id

            val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)

            //Insert SubmissionSubmitter
            submissionSubmitterDao.insert(submissionId, submitterId)

            val contracts = mutableListOf(REPORTABLE, FAVORABLE)
            if (apiId != null) {
                it.attach(ApiSubmissionDao::class.java).insert(submissionId, apiId)
                contracts.add(API)
            } else {
                contracts.add(VOTABLE)
            }

            //Insert Restaurant
            val restaurant = it
                    .attach(RestaurantDao::class.java)
                    .insert(submissionId, restaurantName, latitude, longitude, ownerId)

            //Insert all RestaurantCuisine associations
            insertRestaurantCuisines(it, submissionId, cuisineNames)

            //Insert contracts (VOTABLE,  API if there is an apiId, REPORTABLE if it doesn't)
            it
                    .attach(SubmissionContractDao::class.java)
                    .insertAll(contracts.map { contract -> SubmissionContractParam(submissionId, contract.toString()) })

            return@inTransaction restaurant
        }
    }

    private fun insertRestaurantCuisines(it: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        if (cuisineNames.isEmpty()) return

        val cuisineIds = getCuisinesByNames(cuisineNames, isolationLevel).map { it.submission_id }
        it
                .attach(RestaurantCuisineDao::class.java)
                .insertAll(cuisineIds.map { DbRestaurantCuisineDto(submissionId, it) })
    }
}