package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.INGREDIENT
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val mealDaoClass = MealDao::class.java

@Repository
class MealDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

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
            ingredients: List<Ingredient> = emptyList(),
            foodApi: FoodApiType
    ): MealDto {
        return jdbi.inTransaction<MealDto, Exception>(isolationLevel) {

            //Insert submission
            val mealSubmissionId = it.attach(SubmissionDao::class.java)
                    .insert(MEAL.toString())
                    .submission_id

            //Insert contracts (VOTABLE, REPORTABLE and API if there is an apiId)
            it.attach(SubmissionContractDao::class.java)
                    .insertAll(
                            mutableListOf(VOTABLE, REPORTABLE)
                                    .also { if (apiId != null) it.add(API) }
                                    .map { SubmissionContractParam(mealSubmissionId, it.toString()) }
                    )

            //Insert SubmissionSubmitter associations
            it.attach(SubmissionSubmitterDao::class.java)
                    .insert(mealSubmissionId, submitterId)

            //Insert Meal
            val mealDto = it.attach(mealDaoClass)
                    .insert(mealSubmissionId, mealName)

            //Insert all MealCuisine associations
            it.attach(MealCuisineDao::class.java)
                    .insertAll(cuisines.map { MealCuisineParam(mealSubmissionId, it) })

            //Get API submitter id
            val apiSubmitterId = it.attach(ApiDao::class.java)
                    .getByName(foodApi.toString())!!.submitter_id

            //Insert API meal
            if (apiId != null) {
                insertApiMeal(it, mealSubmissionId, apiSubmitterId, apiId)
            }

            //Insert meal's API ingredients
            if (ingredients.isNotEmpty()) {
                insertMealIngredients(it, mealSubmissionId, apiSubmitterId, ingredients)
            }
            return@inTransaction mealDto
        }
    }

    fun delete(submitterId: Int, submissionId: Int) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this meal
            requireSubmissionSubmitter(submitterId, submissionId, isolationLevel)

            // Check if the submission is a Meal
            requireSubmission(submissionId, MEAL, isolationLevel)

            // Delete all MealCuisine associations
            it.attach(MealCuisineDao::class.java).deleteAllByMealId(submissionId)

            // Delete all MealIngredient associations
            it.attach(MealIngredientDao::class.java).deleteAllByMealId(submissionId)

            // Delete all meal portions
            it.attach(RestaurantMealPortionDao::class.java).deleteAllByMealId(submissionId)

            // Delete meal
            it.attach(mealDaoClass).delete(submissionId)

            // Delete all submitters from this submission (API and User)
            it.attach(SubmissionSubmitterDao::class.java).deleteAllBySubmissionId(submissionId)

            // Delete all submission contracts
            it.attach(SubmissionContractDao::class.java).deleteAllById(submissionId)

            // Delete all user reports
            it.attach(ReportDao::class.java).deleteAllBySubmissionId(submissionId)

            // Delete all user votes
            it.attach(VoteDao::class.java).deleteAllById(submissionId)

            // Delete api submission relation
            if(isFromApi(submissionId)) {
                it.attach(ApiSubmissionDao::class.java).deleteById(submissionId)
            }

            // Delete submission
            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }

    fun update(submitterId: Int, submissionId: Int, name: String, cuisines: List<String> = emptyList()) {
        jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this meal
            requireSubmissionSubmitter(submitterId, submissionId, isolationLevel)

            // Check if the submission is a Meal
            requireSubmission(submissionId, MEAL, isolationLevel)

            // Update meal name
            it.attach(MealDao::class.java).update(submissionId, name)

            // Update cuisines
            if (cuisines.isNotEmpty()) {
                updateCuisines(it, submissionId, cuisines)
            }
        }
    }

    private fun insertApiMeal(handle: Handle, mealSubmissionId: Int, apiSubmitterId: Int, apiId: Int) {
        //If this meal comes from an external API
        handle.attach(SubmissionSubmitterDao::class.java)
                .insert(mealSubmissionId, apiSubmitterId)

        //Insert api SubmissionSubmitter for the meal
        handle.attach(ApiSubmissionDao::class.java)
                .insert(mealSubmissionId, apiId)
    }

    private fun insertMealIngredients(handle: Handle, mealSubmissionId: Int, apiSubmitterId: Int, ingredients: List<Ingredient>) {
        //Get all API ids from
        val insertedApiIngredientIds = handle.attach(IngredientDao::class.java)
                .getAllApiIdsBySubmitterId(apiSubmitterId)
                .toMutableList()

        //Get ingredient API ids from missing ingredient insertions
        val missingIngredients = ingredients
                .filter { !insertedApiIngredientIds.contains(it.apiId) }

        if(missingIngredients.isNotEmpty()) {
            //Insert all new submissions
            val insertedIngredientSubmissionIds = insertAllIngredientSubmissionsAndGet(handle, missingIngredients.size)

            //Update inserted ingredient submission ids
            insertedApiIngredientIds.addAll(insertedIngredientSubmissionIds)

            //Insert all Submission - Submitter associations
            insertAllIngredientSubmissionSubmitter(handle, insertedIngredientSubmissionIds, apiSubmitterId)

            //Insert all SubmissionContracts (API)
            insertAllIngredientContracts(handle, insertedIngredientSubmissionIds)

            //Insert new ingredients
            insertAllIngredients(handle, insertedIngredientSubmissionIds, missingIngredients.map { it.name })

            //Insert all new Submission - ApiSubmission associations
            insertAllIngredientApiSubmission(handle, insertedIngredientSubmissionIds, missingIngredients.map { it.apiId })
        }

        //Insert all new ingredient - meal associations
        insertAllMealIngredient(handle, mealSubmissionId, insertedApiIngredientIds)
    }

    private fun insertAllIngredientSubmissionsAndGet(handle: Handle, count: Int): List<Int> {
        //Insert a new submission for each new ingredient
        return handle.attach(SubmissionDao::class.java).insertAll(
                (1..count).map { SubmissionParam(INGREDIENT.toString()) }
        ).map { it.submission_id }
    }

    private fun insertAllIngredientSubmissionSubmitter(handle: Handle, submissionIds: List<Int>, submitterId: Int) {
        handle.attach(SubmissionSubmitterDao::class.java).insertAll(
                submissionIds.map {
                    SubmissionSubmitterParam(it, submitterId)
                }
        )
    }

    private fun insertAllIngredientContracts(handle: Handle, ingredientSubmissionIds: List<Int>) {
        //Insert contracts
        handle.attach(SubmissionContractDao::class.java)
                .insertAll(ingredientSubmissionIds.map { SubmissionContractParam(it, API.toString()) })
    }

    private fun insertAllMealIngredient(handle: Handle, mealSubmissionId: Int, ingredientSubmissionIds: List<Int>) {
        handle.attach(MealIngredientDao::class.java)
                .insertAll(ingredientSubmissionIds.map { MealIngredientParam(mealSubmissionId, it) })
    }

    private fun insertAllIngredients(handle: Handle, submissionIds: List<Int>, ingredientNames: List<String>) {
        //Insert all new ingredients
        handle.attach(IngredientDao::class.java).insertAll(
                //Zip ingredient submission ids with ingredient name list
                submissionIds.zip(ingredientNames) { submission, ingredientName ->
                    IngredientParam(submission, ingredientName)
                }
        )
    }

    private fun insertAllIngredientApiSubmission(handle: Handle, submissionIds: List<Int>, apiIds: List<Int>) {
        //Insert all Submission - ApiSubmission associations
        handle.attach(ApiSubmissionDao::class.java).insertAll(
                submissionIds.zip(apiIds) { submissionId, apiId ->
                    ApiSubmissionParam(submissionId, apiId)
                }
        )
    }

    private fun updateCuisines(handle: Handle, submissionId: Int, cuisines: List<String>) {
        val mealCuisineDao = handle.attach(MealCuisineDao::class.java)

        //Get existing cuisines
        val existingMealCuisines = mealCuisineDao.getByMealId(submissionId)
                .toMutableList()

        //Delete cuisines
        val deletedCuisines = existingMealCuisines
                .filter { !cuisines.contains(it.cuisine_name) }
        if (deletedCuisines.isNotEmpty()) {
            mealCuisineDao.deleteAllByMealIdAndCuisine(
                    submissionId,
                    deletedCuisines.map { it.cuisine_name }
            )
        }

        //Insert new cuisines
        existingMealCuisines.removeAll(deletedCuisines)
        mealCuisineDao.insertAll(
                existingMealCuisines.map {
                    MealCuisineParam(it.meal_submission_id, it.cuisine_name)
                }
        )
    }
}