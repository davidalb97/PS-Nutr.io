package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.PortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantMealDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val restaurantMealDao = RestaurantMealDao::class.java

@Repository
class RestaurantMealDbRepository(jdbi: Jdbi) : SubmissionDbRepository(jdbi) {

    private val contracts = listOf(REPORTABLE, VOTABLE, FAVORABLE)

    fun getAllMealsFromRestaurant(restaurantId: Int): Sequence<DbRestaurantMealDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbRestaurantMealDto>, Exception>(isolationLevel) { handle ->
                handle.attach(RestaurantMealDao::class.java).getAllByRestaurantId(restaurantId)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun insert(
            submitterId: Int,
            mealId: Int,
            restaurantId: Int
    ): DbRestaurantMealDto {
        return jdbi.inTransaction<DbRestaurantMealDto, Exception>(isolationLevel) {
            // Check if the mealId is from a Meal
            requireSubmission(mealId, MEAL, isolationLevel)

            // Check if the restaurantId is from a Restaurant
            requireSubmission(restaurantId, RESTAURANT, isolationLevel)

            val restaurantMealDao = it.attach(restaurantMealDao)
            val existingRestaurantMeal = restaurantMealDao
                    .getByRestaurantAndMealId(restaurantId, mealId)
            if (existingRestaurantMeal != null) {
                throw InvalidInputException(InvalidInputDomain.RESTAURANT_MEAL,
                        "The restaurant with id $restaurantId already has a meal with id $mealId!"
                )
            }

            val submissionId = it.attach(SubmissionDao::class.java)
                    .insert(RESTAURANT_MEAL.toString())
                    .submission_id

            it.attach(SubmissionSubmitterDao::class.java).insert(submissionId, submitterId)

            return@inTransaction restaurantMealDao.insert(submissionId, restaurantId, mealId)
        }
    }

    fun remove(
            submitterId: Int,
            submissionId: Int
    ) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submissionId is from a RestaurantMeal
            requireSubmission(submissionId, RESTAURANT_MEAL, isolationLevel)

            // Check if the submitter is the submission owner
            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

            // Delete all portions associated with this RestaurantMeal
            it.attach(PortionDao::class.java).getAllByRestaurantMealId(submissionId)

            // Delete restaurant meal association
            it.attach(restaurantMealDao).deleteById(submissionId)

            // Removes submission, submitter association, contracts & it's tables
            super.removeSubmission(submissionId, submitterId, RESTAURANT_MEAL, contracts, isolationLevel)
        }
    }
}