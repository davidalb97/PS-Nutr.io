package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*

@Repository
class DbMealRepository(private val jdbi: Jdbi) {

    fun newMeal(
            submitterId: Int,
            mealName: String,
            apiId: Int? = null,
            cuisines: List<String> = emptyList(),
            ingredientIds: List<Int> = emptyList(),
            foodApi: FoodApiType? = null
    ): Boolean {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Boolean, Exception>(TransactionIsolationLevel.SERIALIZABLE) {

                val submissionDao = it.attach(SubmissionDao::class.java)
                val submissionId = submissionDao.insert(SubmissionType.Meal.name)

                val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)
                submissionSubmitterDao.insert(submissionId, submitterId)

                val mealDao = it.attach(MealDao::class.java)
                mealDao.insert(submissionId, mealName)

                val cuisineDao = it.attach(CuisineDao::class.java)
                cuisines.forEach(cuisineDao::insert)

                if(foodApi != null) {
                    //A meal made out of ingredients does not have apiId as the meal itself does not exist
                    if(apiId == null) {
                        val mealIngredientDao = it.attach(MealIngredientDao::class.java)
                        ingredientIds.forEach{ mealIngredientDao.insert(it, submissionId) }
                    }
                    //If the meal originated from an external API
                    else {
                        val apiDao = it.attach(ApiDao::class.java)
                        val apiSubmitterId = apiDao.getIdByName(foodApi.toString())
                        submissionSubmitterDao.insert(submissionId, apiSubmitterId)

                        val apiSubmissionDao = it.attach(ApiSubmissionDao::class.java)
                        apiSubmissionDao.insert(submissionId, apiId, foodApi.toString())
                    }
                }

                true
            }
        }
    }
}