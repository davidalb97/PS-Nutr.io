package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.INGREDIENT
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val mealDaoClass = MealDao::class.java

@Repository
class MealDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi, isolationLevel) {

    fun getById(submissionId: Int): MealDto? {
        return jdbi.inTransaction<MealDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealDaoClass).getById(submissionId)
        }
    }

    fun getByName(mealName: String): List<MealDto> {
        return jdbi.inTransaction<List<MealDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealDaoClass).getByName(mealName)
        }
    }

    fun insert(
            submitterId: Int,
            mealName: String,
            apiId: Int? = null,
            cuisines: List<String> = emptyList(),
            ingredientIds: List<Ingredient> = emptyList(),
            foodApi: FoodApiType? = null
    ): MealDto {
        return jdbi.inTransaction<MealDto, Exception>(isolationLevel) {

            val submissionDao = it.attach(SubmissionDao::class.java)
            val mealSubmissionId = submissionDao
                    .insert(MEAL.name)
                    .submission_id

            val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)
            submissionSubmitterDao.insert(mealSubmissionId, submitterId)

            val mealDto = it.attach(mealDaoClass)
                    .insert(mealSubmissionId, mealName)

            it.attach(CuisineDao::class.java).insertAll(cuisines.map(::CuisineParam))

            if (foodApi != null) {

                //----------------Api Submission---------------

                val apiSubmitterId = it.attach(ApiDao::class.java)
                        .getByName(foodApi.toString())!!.submitter_id
                submissionSubmitterDao.insert(mealSubmissionId, apiSubmitterId)

                //If this meal comes from an external API
                val apiSubmissionDao = it.attach(ApiSubmissionDao::class.java)
                if (apiId != null) {
                    apiSubmissionDao.insert(mealSubmissionId, apiId, foodApi.toString())
                }

                if (ingredientIds.isEmpty()) {
                    return@inTransaction mealDto
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
                        apiIdsToInsert.map { SubmissionParam(INGREDIENT.name) }
                )

                //Insert all Submission - Submitter associations
                submissionSubmitterDao.insertAll(
                        ingredientSubmissionIds.map {
                            SubmissionSubmitterParam(it.submission_id, apiSubmitterId)
                        }
                )

                //Insert all new ingredients
                ingredientDao.insertAll(
                        //Zip ingredient submission ids with new ingredient list
                        ingredientSubmissionIds.zip(apiIdsToInsert) { submission, ingredient ->
                            IngredientParam(submission.submission_id, ingredient.name)
                        }
                )

                //Insert all submission - ingredient associations
                val mealIngredientDao = it.attach(MealIngredientDao::class.java)
                mealIngredientDao.insertAll(
                        ingredientSubmissionIds.map { MealIngredientParam(mealSubmissionId, it.submission_id) }
                )

                //Insert all submission - api submission associations
                apiSubmissionDao.insertAll(
                        ingredientSubmissionIds.zip(apiIdsToInsert) { submission, ingredient ->
                            ApiSubmissionParam(submission.submission_id, ingredient.apiId, ingredient.name)
                        }
                )
            }
            return@inTransaction mealDto
        }
    }

    fun delete(
            submitterId: Int,
            submissionId: Int
    ) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this meal
            requireSubmissionSubmitter(submitterId, submissionId)

            // Check if the submission is a Restaurant
            requireSubmission(submissionId, MEAL)

            it.attach(MealDao::class.java).delete(submissionId)

            it.attach(SubmissionSubmitterDao::class.java).delete(submissionId)

            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }


    fun update(
            submitterId: Int,
            submissionId: Int,
            name: String
    ) {
        jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this meal
            requireSubmissionSubmitter(submitterId, submissionId)

            // Check if the submission is a Restaurant
            requireSubmission(submissionId, MEAL)

            it.attach(MealDao::class.java).update(submissionId, name)
        }
    }
}