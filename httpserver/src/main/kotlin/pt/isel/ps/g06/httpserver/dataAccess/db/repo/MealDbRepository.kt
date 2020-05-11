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
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.IngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient
import pt.isel.ps.g06.httpserver.springConfig.dto.DbEditableDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val mealDaoClass = MealDao::class.java
private val mealCuisineDaoClass = MealCuisineDao::class.java

@Repository
class MealDbRepository(jdbi: Jdbi, val config: DbEditableDto) : BaseDbRepo(jdbi) {

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

    fun getByHereCuisinesIdentifiers(cuisines: Collection<String>): Collection<MealDto> {
        return jdbi.inTransaction<Collection<MealCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealCuisineDaoClass).getByHereCuisines(cuisines)
        }
    }

    fun getMealsForCuisines(cuisines: Collection<String>): Collection<MealDto> {
        return jdbi.inTransaction<Collection<MealCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealCuisineDaoClass).getByCuisines(cuisines)
        }
    }

    fun insert(
            submitterId: Int,
            mealName: String,
            apiId: String? = null,
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

            // Check if the submission is modifiable
            requireEditable(submissionId, config.`edit-timeout-minutes`!!, isolationLevel)

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
            if (isFromApi(submissionId)) {
                it.attach(ApiSubmissionDao::class.java).deleteById(submissionId)
            }

            // Delete submission
            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }

    fun update(submitterId: Int,
               submissionId: Int,
               name: String,
               cuisines: List<String> = emptyList(),
               ingredients: List<Ingredient> = emptyList()
    ) {
        jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this meal
            requireSubmissionSubmitter(submitterId, submissionId, isolationLevel)

            // Check if the submission is a Meal
            requireSubmission(submissionId, MEAL, isolationLevel)

            // Check if the submission is modifiable
            requireEditable(submissionId, config.`edit-timeout-minutes`!!, isolationLevel)

            // Check if submission was from user (only meals made out of ingredients - without apiId can be updated)
            requireFromUser(submissionId, isolationLevel)

            // Update meal name
            it.attach(MealDao::class.java).update(submissionId, name)

            // Update cuisines
            if (cuisines.isNotEmpty()) {
                updateCuisines(it, submissionId, cuisines)
            }

            // Update ingredients
            if (ingredients.isNotEmpty()) {
                updateIngredients(it, submissionId, ingredients)
            }
        }
    }

    private fun updateIngredients(it: Handle, submissionId: Int, ingredients: List<Ingredient>) {
        val apiSubmitterId = getApiSubmitterIdByMealId(it, submissionId)

        deleteMissingIngredientsFromMeal(it, submissionId, apiSubmitterId, ingredients)

        insertMealIngredients(it, submissionId, apiSubmitterId, ingredients)
    }

    private fun deleteMissingIngredientsFromMeal(it: Handle, submissionId: Int, apiSubmitterId: Int, ingredients: List<Ingredient>) {
        val ingredientApiIds = ingredients.map { it.apiId }
        val deleteIngredientIds = getExistingIngredients(it, apiSubmitterId, ingredients)
                .filter { !ingredientApiIds.contains(it.apiId) }
                .map { it.submission_id }

        if(deleteIngredientIds.isNotEmpty()) {
            it.attach(MealIngredientDao::class.java)
                    .deleteAllByMealIdAndIngredientIds(submissionId, deleteIngredientIds)
        }
    }

    private fun getApiSubmitterIdByMealId(it: Handle, submissionId: Int): Int {
        return it.attach(MealIngredientDao::class.java)
                .getAllByMealId(submissionId).first().ingredient_submission_id
    }

    private fun insertApiMeal(handle: Handle, mealSubmissionId: Int, apiSubmitterId: Int, apiId: String) {
        //If this meal comes from an external API
        handle.attach(SubmissionSubmitterDao::class.java)
                .insert(mealSubmissionId, apiSubmitterId)

        //Insert api SubmissionSubmitter for the meal
        handle.attach(ApiSubmissionDao::class.java)
                .insert(mealSubmissionId, apiId)
    }

    private fun insertMealIngredients(handle: Handle, mealSubmissionId: Int, apiSubmitterId: Int, ingredients: List<Ingredient>) {

        val existingApiIngredientDtos = getExistingIngredients(handle, apiSubmitterId, ingredients)

        //Get ingredient API ids from missing ingredient insertions
        val existingIngredientApiIds = existingApiIngredientDtos
                .map { it.apiId }

        val missingIngredients = ingredients
                .filter { !existingIngredientApiIds.contains(it.apiId) }

        val existingIngredientIds = existingApiIngredientDtos.map { it.submission_id }
                .toMutableList()
        if (missingIngredients.isNotEmpty()) {
            //Insert all new submissions
            val insertedIngredientSubmissionIds = insertAllIngredientSubmissionsAndGet(handle, missingIngredients.size)

            //Update inserted ingredient submission ids
            existingIngredientIds.addAll(insertedIngredientSubmissionIds)

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
        insertAllMealIngredient(handle, mealSubmissionId, existingIngredientIds)
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
        val existingMealIngredientIds = handle.attach(MealIngredientDao::class.java)
                .getAllByMealId(mealSubmissionId).map { it.ingredient_submission_id }
        val newIngredientSubmissionIds = ingredientSubmissionIds.filter {
            !existingMealIngredientIds.contains(it)
        }
        handle.attach(MealIngredientDao::class.java)
                .insertAll(newIngredientSubmissionIds.map { MealIngredientParam(mealSubmissionId, it) })
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

    private fun insertAllIngredientApiSubmission(handle: Handle, submissionIds: List<Int>, apiIds: List<String>) {
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
        val newCuisines = cuisines.filter { cuisineName ->
            existingMealCuisines.none { it.cuisine_name == cuisineName }
        }
        if(newCuisines.isNotEmpty()) {
            mealCuisineDao.insertAll(newCuisines.map { MealCuisineParam(submissionId, it) })
        }
    }

    private fun getExistingIngredients(handle: Handle, apiSubmitterId: Int, ingredients: List<Ingredient>): List<ApiSubmissionDto> {
        //Get already inserted ingredient dtos
        val ingredientApiIds = ingredients.map { it.apiId }
        return handle.attach(ApiSubmissionDao::class.java)
                // Api ingredient api dtos already present on this meal
                .getAllBySubmitterIdSubmissionTypeAndApiIds(apiSubmitterId, INGREDIENT.toString(), ingredientApiIds)
    }

    private fun getDtosFromIngredientNames(
            handle: Handle,
            apiSubmitter: Int,
            ingredientNames: List<String>
    ): List<IngredientDto> {

        return handle.attach(IngredientDao::class.java)
                .getAllBySubmitterId(apiSubmitter)
                .filter { ingredientNames.contains(it.ingredient_name) }
    }
}