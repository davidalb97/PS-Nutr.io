package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient

@Repository
class DbMealRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE

    fun newMeal(
            submitterId: Int,
            mealName: String,
            apiId: Int? = null,
            cuisines: List<String> = emptyList(),
            ingredientIds: List<Ingredient> = emptyList(),
            foodApi: FoodApiType? = null
    ): Int? {
        return inTransaction<Int>(jdbi, serializable) {

            val submissionDao = it.attach(SubmissionDao::class.java)
            val mealSubmissionId = submissionDao
                    .insert(SubmissionType.Meal.name)
                    .submission_id

            val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)
            submissionSubmitterDao.insert(mealSubmissionId, submitterId)

            val mealDao = it.attach(MealDao::class.java)
            mealDao.insert(mealSubmissionId, mealName)

            val cuisineDao = it.attach(CuisineDao::class.java)
            cuisineDao.insertAll(cuisines.map(::CuisineParam).toList())

            if (foodApi != null) {

                //----------------Api Submission---------------

                val apiDao = it.attach(ApiDao::class.java)
                val apiSubmitterId = apiDao.getByName(foodApi.toString())!!.submitter_id
                submissionSubmitterDao.insert(mealSubmissionId, apiSubmitterId)

                //If this meal comes from an external API
                val apiSubmissionDao = it.attach(ApiSubmissionDao::class.java)
                if (apiId != null) {
                    apiSubmissionDao.insert(mealSubmissionId, apiId, foodApi.toString())
                }


                if (ingredientIds.isEmpty()) {
                    return@inTransaction mealSubmissionId
                }

                //----------------Ingredients---------------

                val ingredientDao = it.attach(IngredientDao::class.java)

                //Get all API ids from
                val insertedApiIngredientIds = ingredientDao.getAllApiIdsBySubmitterId(apiSubmitterId)

                //Get ingredient API ids from missing ingredient insertions
                val apiIdsToInsert = ingredientIds
                        .filter { !insertedApiIngredientIds.contains(it.apiId) }

                //Insert a new submission for each new ingredient
                val ingredientSubmissionIds = submissionDao.insertAll(
                        apiIdsToInsert.map { SubmissionParam(SubmissionType.Ingredient.name) }.toList()
                )

                //Insert all Submission - Submitter associations
                submissionSubmitterDao.insertAll(
                        ingredientSubmissionIds.map {
                            SubmissionSubmitterParam(it.submission_id, apiSubmitterId)
                        }.toList()
                )

                //Insert all new ingredients
                ingredientDao.insertAll(
                        //Zip ingredient submission ids with new ingredient list
                        ingredientSubmissionIds.zip(apiIdsToInsert) { submission, ingredient ->
                            IngredientParam(submission.submission_id, ingredient.name)
                        }.toList()
                )

                //Insert all submission - ingredient associations
                val mealIngredientDao = it.attach(MealIngredientDao::class.java)
                mealIngredientDao.insertAll(
                        ingredientSubmissionIds.map { MealIngredientParam(mealSubmissionId, it.submission_id) }.toList()
                )

                //Insert all submission - api submission associations
                apiSubmissionDao.insertAll(
                        ingredientSubmissionIds.zip(apiIdsToInsert) { submission, ingredient ->
                            ApiSubmissionParam(submission.submission_id, ingredient.apiId, ingredient.name)
                        }.toList()
                )
            }
            mealSubmissionId
        }
    }
}