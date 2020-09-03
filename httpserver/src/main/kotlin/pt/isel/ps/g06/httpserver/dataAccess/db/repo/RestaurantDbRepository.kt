package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.util.asCachedSequence

private val restaurantDaoClass = RestaurantDao::class.java

@Repository
class RestaurantDbRepository(private val databaseContext: DatabaseContext, private val cuisineDbRepository: CuisineDbRepository) {
    fun getAllByCoordinates(latitude: Float, longitude: Float, radius: Int): Sequence<DbRestaurantDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(RestaurantDao::class.java)
                    .getByCoordinates(latitude, longitude, radius)
                    .asCachedSequence()
        }
    }

    fun getById(restaurantId: Int): DbRestaurantDto? {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(RestaurantDao::class.java)
                    .getBySubmissionId(restaurantId)
        }
    }

    fun getApiRestaurant(apiSubmitterId: Int, apiId: String): DbRestaurantDto? {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(restaurantDaoClass)
                    .getApiRestaurant(apiSubmitterId, apiId)
        }
    }

    fun insert(
            submitterId: Int,
            restaurantName: String,
            apiId: String? = null,
            cuisineNames: Collection<String> = emptyList(),
            latitude: Float,
            longitude: Float,
            ownerId: Int?
    ): DbRestaurantDto {
        return databaseContext.inTransaction { handle ->
            //Insert Submission
            val submissionId = handle
                    .attach(SubmissionDao::class.java)
                    .insert(RESTAURANT.toString())
                    .submission_id

            val submissionSubmitterDao = handle.attach(SubmissionSubmitterDao::class.java)

            //Insert SubmissionSubmitter
            submissionSubmitterDao.insert(submissionId, submitterId)

            val contracts = mutableListOf(REPORTABLE, FAVORABLE)
            if (apiId != null) {
                handle.attach(ApiSubmissionDao::class.java).insert(submissionId, apiId)
                contracts.add(API)
            } else {
                contracts.add(VOTABLE)
            }

            //Insert Restaurant
            val restaurant = handle
                    .attach(RestaurantDao::class.java)
                    .insert(submissionId, restaurantName, latitude, longitude, ownerId)

            //Insert all RestaurantCuisine associations
            if (!cuisineNames.isEmpty()) {
                //TODO Make better
                val cuisines = cuisineDbRepository
                        .getAllByNames(cuisineNames.asSequence())
                        .map { DbRestaurantCuisineDto(submissionId, it.submission_id) }

                //TODO Avoid eager call
                handle.attach(RestaurantCuisineDao::class.java)
                        .insertAll(cuisines.toList())
            }

            //Insert contracts (REPORTABLE, API if there is an apiId, VOTABLE if it doesn't)
            handle
                    .attach(SubmissionContractDao::class.java)
                    .insertAll(contracts.map { contract -> SubmissionContractParam(submissionId, contract.toString()) })

            return@inTransaction restaurant
        }
    }

    fun addOwner(restaurantId: Int, ownerId: Int): DbRestaurantDto {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(restaurantDaoClass)
                    .addOwner(restaurantId, ownerId)
        }
    }
}