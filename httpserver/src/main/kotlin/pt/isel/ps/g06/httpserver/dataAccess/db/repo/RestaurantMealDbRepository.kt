package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT_MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.exception.problemJson.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.exception.problemJson.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.util.asCachedSequence
import java.util.*

private val restaurantMealDao = RestaurantMealDao::class.java

@Repository
class RestaurantMealDbRepository(
        private val databaseContext: DatabaseContext,
        private val restaurantDbRepository: RestaurantDbRepository,
        private val mealDbRepository: MealDbRepository
) {
    fun getRestaurantMeal(restaurantId: Int, mealId: Int): DbRestaurantMealDto? {
        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(restaurantMealDao)
                    .getByRestaurantAndMealId(restaurantId, mealId)
        }
    }

    fun getAllUserFavorites(submitterId: Int, count: Int?, skip: Int?): Sequence<DbRestaurantMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(restaurantMealDao)
                    .getAllUserFavorites(submitterId, count, skip)
                    .asCachedSequence()
        }
    }

    fun insert(
            submitterId: Int?,
            mealId: Int,
            restaurantId: Int,
            verified: Boolean
    ): DbRestaurantMealDto {
        return databaseContext.inTransaction {
            // Check if the mealId is from a Meal
            restaurantDbRepository.getById(restaurantId) ?: throw RestaurantNotFoundException()
            mealDbRepository.getById(mealId) ?: throw MealNotFoundException()

            val restaurantMealDao = it.attach(restaurantMealDao)


            //Check if restaurantMeal already exists
            if (restaurantMealDao.getByRestaurantAndMealId(restaurantId, mealId) != null) {
                //TODO Change exception
                throw InvalidInputException("The restaurant with id $restaurantId already has a meal with id $mealId!")
            }

            val submissionId = it
                    .attach(SubmissionDao::class.java)
                    .insert(RESTAURANT_MEAL.toString())
                    .submission_id

            val contracts = EnumSet.of(FAVORABLE, VOTABLE)
            if (submitterId != null) {
                it.attach(SubmissionSubmitterDao::class.java).insert(submissionId, submitterId)
                contracts.add(REPORTABLE)
            }

            //Insert all needed contracts
            it.attach(SubmissionContractDao::class.java).insertAll(contracts.map { contract ->
                SubmissionContractParam(
                        submissionId,
                        contract.toString()
                )
            })

            return@inTransaction restaurantMealDao.insert(submissionId, restaurantId, mealId, verified)
        }
    }

    fun putVerification(submissionId: Int, verification: Boolean): DbRestaurantMealDto {
        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(restaurantMealDao)
                    .updateRestaurantMealVerification(submissionId, verification)
        }
    }
}