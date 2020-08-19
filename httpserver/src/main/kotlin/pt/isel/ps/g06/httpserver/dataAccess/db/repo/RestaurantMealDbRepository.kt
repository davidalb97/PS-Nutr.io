package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidInputDomain
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidInputException
import java.util.*

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val restaurantMealDao = RestaurantMealDao::class.java

@Repository
class RestaurantMealDbRepository(jdbi: Jdbi) : SubmissionDbRepository(jdbi) {
    fun getRestaurantMeal(restaurantId: Int, mealId: Int): DbRestaurantMealDto? {
        return jdbi.inTransaction<DbRestaurantMealDto, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(restaurantMealDao)
                    .getByRestaurantAndMealId(restaurantId, mealId)
        }
    }

    fun insert(
            submitterId: Int?,
            mealId: Int,
            restaurantId: Int
    ): DbRestaurantMealDto {
        return jdbi.inTransaction<DbRestaurantMealDto, Exception>(isolationLevel) {
            // Check if the mealId is from a Meal
            requireSubmission(mealId, MEAL, isolationLevel)

            // Check if the restaurantId is from a Restaurant
            requireSubmission(restaurantId, RESTAURANT, isolationLevel)

            val restaurantMealDao = it.attach(restaurantMealDao)

            //Check if restaurantMeal already exists
            val existingRestaurantMeal = restaurantMealDao
                    .getByRestaurantAndMealId(restaurantId, mealId)

            if (existingRestaurantMeal != null) {
                throw InvalidInputException(InvalidInputDomain.RESTAURANT_MEAL,
                        "The restaurant with id $restaurantId already has a meal with id $mealId!"
                )
            }

            val submissionId = it
                    .attach(SubmissionDao::class.java)
                    .insert(RESTAURANT_MEAL.toString())
                    .submission_id

            val contracts = EnumSet.of(FAVORABLE)
            if (submitterId != null) {
                it.attach(SubmissionSubmitterDao::class.java).insert(submissionId, submitterId)
                contracts.add(REPORTABLE)
                contracts.add(VOTABLE)
            }

            //Insert all needed contracts
            it.attach(SubmissionContractDao::class.java).insertAll(contracts.map { contract ->
                SubmissionContractParam(
                        submissionId,
                        contract.toString()
                )
            })

            return@inTransaction restaurantMealDao.insert(submissionId, restaurantId, mealId)
        }
    }

    // TODO
    fun getVerification() {

    }

    fun putVerification(submissionId: Int, verification: Boolean): DbRestaurantMealDto {
        return jdbi.inTransaction<DbRestaurantMealDto, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(restaurantMealDao)
                    .updateRestaurantMealVerification(submissionId, verification)
        }
    }
}