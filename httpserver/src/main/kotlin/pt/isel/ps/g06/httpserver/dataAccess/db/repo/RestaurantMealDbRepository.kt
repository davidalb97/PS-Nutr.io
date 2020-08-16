package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT_MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import java.util.*

@Repository
class RestaurantMealDbRepository(private val databaseContext: DatabaseContext) {
    fun getRestaurantMeal(restaurantId: Int, mealId: Int): DbRestaurantMealDto? {
        return databaseContext.inTransaction {
            it.attach(RestaurantMealDao::class.java).getByRestaurantAndMealId(restaurantId, mealId)
        }
    }

    fun insert(submitterId: Int?, mealId: Int, restaurantId: Int): DbRestaurantMealDto {
        return databaseContext.inTransaction {
            val restaurantMealDao = it.attach(RestaurantMealDao::class.java)

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
                SubmissionContractParam(submissionId, contract.toString())
            })

            return@inTransaction restaurantMealDao.insert(submissionId, restaurantId, mealId)
        }
    }
}