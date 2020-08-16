package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import java.util.stream.Stream

private val restaurantDaoClass = RestaurantDao::class.java

@Repository
class RestaurantDbRepository(private val databaseContext: DatabaseContext) {
    fun getAllByCoordinates(latitude: Float, longitude: Float, radius: Int): Stream<DbRestaurantDto> {
        return databaseContext.inTransaction {
            it.attach(restaurantDaoClass).getByCoordinates(latitude, longitude, radius)
        }
    }

    fun getById(restaurantId: Int): DbRestaurantDto? {
        return databaseContext.inTransaction {
            it.attach(restaurantDaoClass).getBySubmissionId(restaurantId)
        }
    }

    fun getApiRestaurant(apiSubmitterId: Int, apiId: String): DbRestaurantDto? {
        return databaseContext.inTransaction {
            it.attach(restaurantDaoClass).getApiRestaurant(apiSubmitterId, apiId)
        }
    }


    //TODO Should be restaurant model
    fun insert(
            submitterId: Int,
            restaurantName: String,
            apiId: String? = null,
            cuisineNames: Collection<String> = emptyList(),
            latitude: Float,
            longitude: Float
    ): DbRestaurantDto {
        //TODO Better exception
        if (cuisineNames.isEmpty()) throw IllegalStateException("Restaurant must have cuisines!")

        return databaseContext.inTransaction {
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
                    .attach(restaurantDaoClass)
                    .insert(submissionId, restaurantName, latitude, longitude)

            //Insert all RestaurantCuisine associations
            //TODO Make this better
            insertRestaurantCuisines(it, submissionId, cuisineNames)

            //Insert contracts (VOTABLE, API if there is an apiId, REPORTABLE if it doesn't)
            it
                    .attach(SubmissionContractDao::class.java)
                    .insertAll(contracts.map { contract -> SubmissionContractParam(submissionId, contract.toString()) })

            return@inTransaction restaurant
        }
    }

    private fun insertRestaurantCuisines(it: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        if (cuisineNames.isEmpty()) return

        //TODO
//        val cuisineIds = getCuisinesByNames(cuisineNames, isolationLevel).map { it.submission_id }
//        it
//                .attach(RestaurantCuisineDao::class.java)
//                .insertAll(cuisineIds.map { DbRestaurantCuisineDto(submissionId, it) })
    }
}