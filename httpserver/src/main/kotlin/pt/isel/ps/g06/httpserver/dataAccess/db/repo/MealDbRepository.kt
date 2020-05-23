package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.INGREDIENT
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmitterType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbIngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.model.Ingredient
import pt.isel.ps.g06.httpserver.springConfig.dto.DbEditableDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val mealDaoClass = MealDao::class.java
private val mealCuisineDaoClass = MealCuisineDao::class.java

@Repository
class MealDbRepository(jdbi: Jdbi, val config: DbEditableDto) : BaseDbRepo(jdbi) {

    fun getById(submissionId: Int): DbMealDto? {
        return jdbi.inTransaction<DbMealDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealDaoClass).getById(submissionId)
        }
    }

    fun getByName(mealName: String): DbMealDto? {
        return jdbi.inTransaction<DbMealDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealDaoClass).getByName(mealName)
        }
    }


    fun getAllByCuisineApiIds(foodApiType: FoodApiType, cuisineApiIds: Collection<String>): Collection<DbMealDto> {
        return jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) {
            val apiSubmitterId = it.attach(SubmitterDao::class.java)
                    .getAllByType(SubmitterType.API.toString())
                    .first { it.submitter_name == foodApiType.toString() }
                    .submitter_id
            return@inTransaction it.attach(MealDao::class.java)
                    .getAllByApiSubmitterAndCuisineApiIds(apiSubmitterId, cuisineApiIds)
        }
    }

    fun getAllByCuisineNames(cuisineNames: Collection<String>): Collection<DbMealDto> {
        return jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(MealDao::class.java)
                    .getAllByCuisineNames(cuisineNames)
        }
    }

    /**
     * @throws InvalidInputException On invalid cuisines passed.
     *                               (Annotation required for testing purposes)
     */
    @Throws(InvalidInputException::class)
    fun insert(
            submitterId: Int,
            mealName: String,
            apiId: String? = null,
            cuisineNames: List<String> = emptyList(),
            ingredients: List<Ingredient> = emptyList(),
            foodApi: FoodApiType
    ): DbMealDto {
        return jdbi.inTransaction<DbMealDto, Exception>(isolationLevel) {

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
            insertMealCuisines(it, mealSubmissionId, cuisineNames)

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

    /**
     * @throws InvalidInputException On invalid submission ownership, invalid submission type,
     *                               submission change timed out.
     *                               (Annotation required for testing purposes)
     */
    @Throws(InvalidInputException::class)
    fun delete(submitterId: Int, submissionId: Int) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this meal
            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

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

    /**
     * @throws InvalidInputException On invalid submission ownership, invalid submission type,
     *                               submission change timed out, if it is an api meal
     *                               or invalid cuisines were passed.
     *                               (Annotation required for testing purposes)
     */
    @Throws(InvalidInputException::class)
    fun update(submitterId: Int,
               submissionId: Int,
               name: String,
               cuisineNames: List<String> = emptyList(),
               ingredients: List<Ingredient> = emptyList()
    ) {
        jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submitter is the creator of this meal
            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

            // Check if the submission is a Meal
            requireSubmission(submissionId, MEAL, isolationLevel)

            // Check if the submission is modifiable
            requireEditable(submissionId, config.`edit-timeout-minutes`!!, isolationLevel)

            // Check if submission was from user (only meals made out of ingredients - without apiId can be updated)
            requireFromUser(submissionId, isolationLevel)

            // Update meal name
            it.attach(MealDao::class.java).update(submissionId, name)

            // Update cuisines
            if (cuisineNames.isNotEmpty()) {
                updateCuisines(it, submissionId, cuisineNames)
            }

            // Update ingredients
            if (ingredients.isNotEmpty()) {
                updateIngredients(it, submissionId, ingredients)
            }
        }
    }

    private fun insertMealCuisines(it: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        val cuisineIds = getCuisinesByNames(cuisineNames, isolationLevel)
                .map { it.cuisine_id }
        it.attach(MealCuisineDao::class.java)
                .insertAll(cuisineIds.map { DbMealCuisineDto(submissionId, it) })
    }

    private fun updateIngredients(it: Handle, submissionId: Int, ingredients: List<Ingredient>) {
        val apiSubmitterId = getApiSubmitterIdByMealId(it, submissionId)

        deleteMissingIngredientsFromMeal(it, submissionId, apiSubmitterId, ingredients)

        insertMealIngredients(it, submissionId, apiSubmitterId, ingredients)
    }

    private fun deleteMissingIngredientsFromMeal(it: Handle, submissionId: Int, apiSubmitterId: Int, ingredients: List<Ingredient>) {
        val ingredientApiIds = ingredients.map { it.id }
        val deleteIngredientIds = getExistingIngredients(it, apiSubmitterId, ingredients)
                .filter { !ingredientApiIds.contains(it.apiId) }
                .map { it.submission_id }

        if (deleteIngredientIds.isNotEmpty()) {
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
                .filter { !existingIngredientApiIds.contains(it.id) }

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
            insertAllIngredientApiSubmission(handle, insertedIngredientSubmissionIds, missingIngredients.map { it.id })
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
        if(newIngredientSubmissionIds.isNotEmpty()) {
            handle.attach(MealIngredientDao::class.java)
                    .insertAll(newIngredientSubmissionIds.map {
                        MealIngredientParam(mealSubmissionId, it)
                    })
        }
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

    private fun updateCuisines(handle: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        val cuisineDtos = getCuisinesByNames(cuisineNames, isolationLevel)
        val mealCuisineDao = handle.attach(MealCuisineDao::class.java)

        //Get existing cuisines
        val existingMealCuisineIds = mealCuisineDao.getAllByMealId(submissionId)
                .map { it.cuisine_id }
                .toMutableList()

        //Delete cuisines
        val deletedCuisineIds = existingMealCuisineIds
                .filter { existing -> cuisineDtos.none { it.cuisine_id == existing } }
        if (deletedCuisineIds.isNotEmpty()) {
            mealCuisineDao.deleteAllByMealIdAndCuisineIds(submissionId, deletedCuisineIds)
        }

        //Insert new cuisines
        existingMealCuisineIds.removeAll(deletedCuisineIds)
        val newCuisineIds = cuisineDtos.filter { cuisine ->
            existingMealCuisineIds.none { it == cuisine.cuisine_id }
        }.map { it.cuisine_id }

        if (newCuisineIds.isNotEmpty()) {
            mealCuisineDao.insertAll(newCuisineIds.map { DbMealCuisineDto(submissionId, it) })
        }
    }

    private fun getExistingIngredients(
            handle: Handle,
            apiSubmitterId: Int,
            ingredients: Collection<Ingredient>
    ): Collection<DbApiSubmissionDto> {
        //Get already inserted ingredient dtos
        val ingredientApiIds = ingredients.map { it.id }
        return handle.attach(ApiSubmissionDao::class.java)
                // Api ingredient api dtos already present on this meal
                .getAllBySubmitterIdSubmissionTypeAndApiIds(apiSubmitterId, INGREDIENT.toString(), ingredientApiIds)
    }

    private fun getDtosFromIngredientNames(
            handle: Handle,
            apiSubmitter: Int,
            ingredientNames: Collection<String>
    ): Collection<DbIngredientDto> {

        return handle.attach(IngredientDao::class.java)
                .getAllBySubmitterId(apiSubmitter)
                .filter { ingredientNames.contains(it.ingredient_name) }
    }
}