package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.API
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.FAVORABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.INGREDIENT
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.*
import pt.isel.ps.g06.httpserver.dataAccess.input.IngredientInput
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.model.Ingredient
import pt.isel.ps.g06.httpserver.springConfig.dto.DbEditableDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val mealDaoClass = MealDao::class.java
private val mealCuisineDaoClass = MealCuisineDao::class.java
private val restaurantMealDaoClass = RestaurantMealDao::class.java

@Repository
class MealDbRepository(jdbi: Jdbi, val config: DbEditableDto) : SubmissionDbRepository(jdbi) {

    val contracts = listOf(API, FAVORABLE)

    fun getById(mealSubmissionId: Int): DbMealDto? {
        return jdbi.inTransaction<DbMealDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getById(mealSubmissionId)
        }
    }

    fun getMealIngredients(mealId: Int): Sequence<DbMealIngredientDto> {
        return jdbi.inTransaction<Sequence<DbMealIngredientDto>, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(MealIngredientDao::class.java)
                    .getAllByMealId(mealId).asSequence()
        }
    }

    fun getIngredients(mealId: Int): Sequence<DbIngredientDto> {
        return jdbi.inTransaction<Sequence<DbIngredientDto>, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(IngredientDao::class.java)
                    .getAllByMealId(mealId).asSequence()
            fun getAllByCuisineApiIds(apiSubmitterId: Int, cuisineApiIds: Sequence<String>): Sequence<DbMealDto> {
                val collection = lazy {
                    jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) {
                        val cuisineApiIdsList = cuisineApiIds.toList()
                        if (cuisineApiIdsList.isEmpty())
                            return@inTransaction emptyList()
//                val apiSubmitterId = it.attach(SubmitterDao::class.java)
//                        .getAllByType(SubmitterType.API.toString())
//                        .first { it.submitter_name == foodApiType.toString() }
//                        .submitter_id
                        return@inTransaction it.attach(MealDao::class.java)
                                .getAllByApiSubmitterAndCuisineApiIds(apiSubmitterId, cuisineApiIdsList)
                    }
                }
                return sequence { collection.value.iterator() }
            }

            fun getAllByCuisineNames(cuisineNames: Sequence<String>): Sequence<DbMealDto> {
                val collection = lazy {
                    jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) { handle ->
                        //TODO change to cool name
                        val cuisineNameList = cuisineNames.toList()
                        if (cuisineNameList.isEmpty())
                            return@inTransaction emptyList()
                        return@inTransaction handle.attach(MealDao::class.java)
                                .getAllByCuisineNames(cuisineNameList)
                    }
                }
                return sequence { collection.value.iterator() }
            }

            /**
             * @throws InvalidInputException On invalid cuisines passed.
             *                               (Annotation required for testing purposes)
             */
            @Throws(InvalidInputException::class)
            fun insert(
                    submitterId: Int,
                    mealName: String,
            quantity: Int,
            cuisines: Collection<String>,
            ingredients: Collection<IngredientInput>
    ): DbMealDto {
        if (cuisines.isEmpty()) {
            throw InvalidInputException(InvalidInputDomain.CUISINE, "A meal must have at least a cuisine!")
        }

        if (ingredients.isEmpty()) {
            throw InvalidInputException(InvalidInputDomain.INGREDIENT, "A meal must have at least one ingredient!")
        }

        return jdbi.inTransaction<DbMealDto, Exception>(isolationLevel) {
            //Validate given ingredients to see if they are all in the database
            val databaseIngredients = it
                    .attach(IngredientDao::class.java)
                    .getAllByIds(ingredients.map { ingredient -> ingredient.identifier!! })

            if (databaseIngredients.size != ingredients.size) {
                throw InvalidInputException(
                        InvalidInputDomain.INGREDIENT,
                        "Not all ingredients belong to the database!"
                )
            }

            //Insert submission
            val mealSubmissionId = it.attach(SubmissionDao::class.java)
                    .insert(MEAL.toString())
                    .submission_id

            //Insert contract FAVORABLE
            it.attach(SubmissionContractDao::class.java)
                    .insertAll(mutableListOf(FAVORABLE).map {
                        SubmissionContractParam(mealSubmissionId, it.toString())
                    })

            //Insert SubmissionSubmitter associations for user
            it.attach(SubmissionSubmitterDao::class.java).insert(mealSubmissionId, submitterId)


            //Calculate meal carbs from adding each ingredient carb for given input quantity
            val carbs = databaseIngredients
                    .zip(ingredients, this::getCarbsForInputQuantity)
                    .sum()
                    .toInt()

            //Insert Meal
            val mealDto = it
                    .attach(mealDaoClass)
                    .insert(mealSubmissionId, mealName, carbs, quantity)

            //Insert all MealCuisine associations
            insertMealCuisines(it, mealSubmissionId, cuisines)

            //Insert meal's ingredients
            it.attach(MealIngredientDao::class.java).insertAll(ingredients.map { ingredientInput ->
                DbMealIngredientDto(
                        //TODO Avoid nulls here
                        //We know fields are not null due to validation checks
                        meal_submission_id = mealSubmissionId,
                        ingredient_submission_id = ingredientInput.identifier!!,
                        quantity = ingredientInput.quantity!!,
                        //TODO Read DbMealIngredientDto.unit/carbs from db table!
                        carbs = 100,
                        unit = "grams"
                )
            })

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

            // Check if the submission is modifiable
            requireEditable(submissionId, config.`edit-timeout-minutes`!!, isolationLevel)

            // Delete all MealCuisine associations
            it.attach(MealCuisineDao::class.java).deleteAllByMealId(submissionId)

            // Delete all MealIngredient associations
            it.attach(MealIngredientDao::class.java).deleteAllByMealId(submissionId)

            // Delete RestaurantMeal portions
            deleteMealPortions(it, submissionId)

            // Delete RestaurantMeals from this meal
            it.attach(RestaurantMealDao::class.java).deleteAllByMealId(submissionId)

            // Delete meal
            it.attach(mealDaoClass).delete(submissionId)

            // Removes submission, submitter association, contracts & it's tables
            super.removeSubmission(submissionId, submitterId, MEAL, contracts, isolationLevel)
        }
    }

    /**
     * @throws InvalidInputException On invalid submission ownership, invalid submission type,
     *                               submission change timed out, if it is an api meal,
     *                               invalid cuisines were passed, no cuisines were passed
     *                               or no ingredients were passed.
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
            it.attach(MealDao::class.java).updateName(submissionId, name)

            // Update cuisines
            updateCuisines(it, submissionId, cuisineNames)

            // Update ingredients
            updateIngredients(it, submissionId, ingredients)
        }
    }

    private fun insertMealCuisines(it: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        if (cuisineNames.isEmpty()) return
        //throw InvalidInputException(InvalidInputDomain.CUISINE, "A meal must have at least a cuisine!")
        //TODO Probably can be made better with one single insert (without needing to check for cuisine Ids)
        val cuisineIds = getCuisinesByNames(cuisineNames, isolationLevel).map { it.submission_id }
        it.attach(MealCuisineDao::class.java).insertAll(cuisineIds.map { DbMealCuisineDto(submissionId, it) })
    }

    private fun updateIngredients(it: Handle, submissionId: Int, ingredients: List<Ingredient>) {

        val apiSubmitterId = it.attach(ApiDao::class.java)
                .getSubmitterBySubmissionId(submissionId)!!.submitter_id

        deleteMissingIngredientsFromMeal(it, submissionId, apiSubmitterId, ingredients)

        insertMealIngredients(it, submissionId, apiSubmitterId, ingredients)
    }

    private fun deleteMissingIngredientsFromMeal(it: Handle, submissionId: Int, apiSubmitterId: Int, ingredients: List<Ingredient>) {
        val ingredientApiIds = ingredients.map { it.identifier }
        val deleteIngredientIds = getExistingIngredients(it, apiSubmitterId, ingredients)
                .filter { !ingredientApiIds.contains(it.apiId) }
                .map { it.submission_id }

        if (deleteIngredientIds.isNotEmpty()) {
            it.attach(MealIngredientDao::class.java)
                    .deleteAllByMealIdAndIngredientIds(submissionId, deleteIngredientIds)
        }
    }

    private fun insertMealIngredients(handle: Handle, mealSubmissionId: Int, apiSubmitterId: Int, ingredients: List<Ingredient>) {
        if (ingredients.isEmpty()) {
            throw InvalidInputException(InvalidInputDomain.INGREDIENT,
                    "A custom meal must have at least one ingredient!"
            )
        }

        val existingApiIngredientDtos = getExistingIngredients(handle, apiSubmitterId, ingredients)

        //Get ingredient API ids from missing ingredient insertions
        val existingIngredientApiIds = existingApiIngredientDtos
                .map { it.apiId }

        val missingIngredients = ingredients
                .filter { !existingIngredientApiIds.contains(it.identifier) }

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
            //insertAllIngredients(handle, insertedIngredientSubmissionIds, missingIngredients.map { it.name })

            //Insert all new Submission - ApiSubmission associations
            //insertAllIngredientApiSubmission(handle, insertedIngredientSubmissionIds, missingIngredients.map { it.id })
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
        if (newIngredientSubmissionIds.isNotEmpty()) {
            //TODO Inser all meal ingredients!
//            handle.attach(MealIngredientDao::class.java)
//                    .insertAll(newIngredientSubmissionIds.map {
//                        MealIngredientParam(mealSubmissionId, it, 100) // TODO
//                    })
        }
    }

    /*private fun insertAllIngredients(handle: Handle, submissionIds: List<Int>, ingredientNames: List<String>) {
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
    }*/

    private fun updateCuisines(handle: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        if (cuisineNames.isEmpty()) {
            throw InvalidInputException(InvalidInputDomain.CUISINE, "A meal must have at least a cuisine!")
        }

        val cuisineDtos = getCuisinesByNames(cuisineNames, isolationLevel)
        val mealCuisineDao = handle.attach(MealCuisineDao::class.java)

        //Get existing cuisines
        val existingMealCuisineIds = mealCuisineDao.getAllByMealId(submissionId)
                .map { it.cuisine_submission_id }
                .toMutableList()

        //Delete cuisines
        val deletedCuisineIds = existingMealCuisineIds
                .filter { existing -> cuisineDtos.none { it.submission_id == existing } }
        if (deletedCuisineIds.isNotEmpty()) {
            mealCuisineDao.deleteAllByMealIdAndCuisineIds(submissionId, deletedCuisineIds)
        }

        //Insert new cuisines
        existingMealCuisineIds.removeAll(deletedCuisineIds)
        val newCuisineIds = cuisineDtos.filter { cuisine ->
            existingMealCuisineIds.none { it == cuisine.submission_id }
        }.map { it.submission_id }

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
        val ingredientApiIds = ingredients.map { it.identifier }
        return handle.attach(ApiSubmissionDao::class.java)
                // Api ingredient api dtos already present on this meal
                .getAllBySubmitterIdSubmissionTypeAndApiIds(apiSubmitterId, INGREDIENT.toString(), ingredientApiIds)
    }

    private fun deleteMealPortions(handle: Handle, submissionId: Int) {
        //Get all restaurant meal ids
        val restaurantMealIds = handle.attach(RestaurantMealDao::class.java)
                .getAllByMealId(submissionId)
                .map { it.submission_id }

        //Delete all portions with the restaurant meal ids
        if (restaurantMealIds.isNotEmpty())
            handle.attach(PortionDao::class.java).deleteAllByRestaurantMealIds(restaurantMealIds)
    }

    private fun getCarbsForInputQuantity(dbIngredientDto: DbIngredientDto, ingredientInput: IngredientInput): Float {
        return (ingredientInput.quantity!!.times(dbIngredientDto.carbs).toFloat()).div(dbIngredientDto.amount)
    }

    fun getIngredients(skip: Int?, limit: Int?): Collection<DbMealDto> {
        return jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealDaoClass).getIngredients(skip ?: 0, limit)
        }
    }
}