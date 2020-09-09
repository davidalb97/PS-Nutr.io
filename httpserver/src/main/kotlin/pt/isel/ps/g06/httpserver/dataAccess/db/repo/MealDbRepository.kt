package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.FAVORABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealIngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.input.ingredient.IngredientInput
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.util.asCachedSequence

private val mealDaoClass = MealDao::class.java

@Repository
class MealDbRepository(
        private val databaseContext: DatabaseContext,
        private val cuisineDbRepository: CuisineDbRepository,
        private val submissionDbRepository: SubmissionDbRepository
) {
    fun getById(mealSubmissionId: Int): DbMealDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getById(mealSubmissionId)
        }
    }

    fun getBySubmitterId(submitterId: Int, skip: Int?, count: Int?): Sequence<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getAllBySubmitterIdAndType(submitterId, MealType.CUSTOM.toString(), skip, count)
                    .asCachedSequence()
        }

    }

    fun getAllUserFavorites(submitterId: Int, count: Int?, skip: Int?): Sequence<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getAllUserSuggestedFavorites(submitterId, count, skip)
                    .asCachedSequence()
        }
    }

    fun getMealIngredients(mealId: Int): Sequence<DbMealIngredientDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealIngredientDao::class.java)
                    .getMealIngredientsByMealId(mealId)
                    .asCachedSequence()
        }
    }

    fun getMealComponents(mealId: Int): Sequence<DbMealIngredientDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealIngredientDao::class.java)
                    .getMealComponentsByMealId(mealId)
                    .asCachedSequence()
        }

    }

    fun getAllIngredients(skip: Int?, count: Int?): Sequence<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealDao::class.java)
                    .getAllByType(MEAL_TYPE_SUGGESTED_INGREDIENT, skip, count)
                    .asCachedSequence()
        }

    }

    fun getAllSuggestedMealsByCuisineApiIds(apiSubmitterId: Int, cuisineApiIds: Sequence<String>): Sequence<DbMealDto> {
        val cuisineApiIdsList = cuisineApiIds.toList()
        if (cuisineApiIdsList.isEmpty()) return emptySequence() //TODO See if eager call is possible to avoid

        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(MealDao::class.java)
                    .getAllSuggestedForApiCuisines(apiSubmitterId, cuisineApiIdsList)
                    .asCachedSequence()
        }
    }

    fun getAllSuggestedMealsFromCuisineNames(cuisineNames: Sequence<String>): Sequence<DbMealDto> {
        val cuisineNameList = cuisineNames.toList()
        //TODO See if eager call is possible to avoid
        if (cuisineNameList.isEmpty()) return emptySequence()

        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealDao::class.java)
                    .getAllSuggestedForCuisineNames(cuisineNameList)
                    .asCachedSequence()
        }
    }

    //TODO use cuisine filtering
    fun getAllSuggestedMeals(skip: Int?, count: Int?, cuisines: Collection<String>?): Sequence<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealDao::class.java)
                    //.getAllSuggestedMeals(skip, count, cuisines)
                    .getAllByType(MEAL_TYPE_SUGGESTED_MEAL, skip, count)
                    .asCachedSequence()
        }
    }

    fun getAllUserMealsForRestaurant(restaurantId: Int): Sequence<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealDao::class.java)
                    .getAllUserMealsByRestaurantId(restaurantId)
                    .asCachedSequence()
        }
    }

    fun insertCustomMeal(
            submitterId: Int,
            mealName: String,
            quantity: Float,
            cuisines: Collection<String>,
            ingredients: Collection<IngredientInput>,
            type: MealType
    ): DbMealDto {
        if (ingredients.isEmpty()) {
            throw InvalidInputException("A meal must have at least an ingredient")
        }

        if (cuisines.isEmpty()) {
            throw InvalidInputException("A meal must have at least a cuisine!")
        }

        return databaseContext.inTransaction {
            //Validate given ingredients to see if they are all in the database
            //TODO See if eager call is possible to avoid
            val databaseIngredients = requireIngredientsById(
                    it,
                    ingredients.map { ingredient -> ingredient.identifier!! }
            )

            //Insert submission
            val mealSubmissionId = it.attach(SubmissionDao::class.java)
                    .insert(SubmissionType.MEAL.toString())
                    .submission_id

            if (type == MealType.SUGGESTED_MEAL) {
                //Insert contract FAVORABLE
                it.attach(SubmissionContractDao::class.java)
                        .insertAll(mutableListOf(FAVORABLE).map {
                            SubmissionContractParam(mealSubmissionId, it.toString())
                        })
            }

            //Insert SubmissionSubmitter associations for user
            it.attach(SubmissionSubmitterDao::class.java).insert(mealSubmissionId, submitterId)

            // TODO: Logic should be isolated on the service and passed to repo as a parameter
            //Calculate meal carbs from adding each ingredient carb for given input quantity
            val carbs = databaseIngredients
                    .zip(ingredients, this::getCarbsForInputQuantity)
                    .sum()

            //Insert Meal
            val mealDto = it
                    .attach(mealDaoClass)
                    .insert(mealSubmissionId, mealName, carbs, quantity, mealType = type.toString())

            //Insert all MealCuisine associations
            //TODO Make this better
            val cuisineIds = cuisineDbRepository
                    .getAllByNames(cuisines.asSequence())
                    .map { DbMealCuisineDto(mealSubmissionId, it.submission_id) }

            it.attach(MealCuisineDao::class.java).insertAll(cuisineIds.toList())

            //Insert meal's ingredients
            it.attach(MealIngredientDao::class.java).insertAll(ingredients.map { ingredientInput ->
                DbMealIngredientDto(
                        //We know fields are not null due to validation checks
                        meal_submission_id = mealSubmissionId,
                        ingredient_submission_id = ingredientInput.identifier!!,
                        quantity = ingredientInput.quantity!!
                )
            })

            return@inTransaction mealDto
        }
    }

    fun insertSuggestedMeal(
            submitterId: Int,
            mealName: String,
            quantity: Float,
            carbs: Float,
            cuisines: Collection<String>,
            type: MealType
    ): DbMealDto {

        if (cuisines.isEmpty()) {
            throw InvalidInputException("A meal must have at least a cuisine!")
        }

        return databaseContext.inTransaction {
            //Insert submission
            val mealSubmissionId = it.attach(SubmissionDao::class.java)
                    .insert(SubmissionType.MEAL.toString())
                    .submission_id

            if (type == MealType.SUGGESTED_MEAL) {
                //Insert contract FAVORABLE
                it.attach(SubmissionContractDao::class.java)
                        .insertAll(mutableListOf(FAVORABLE).map {
                            SubmissionContractParam(mealSubmissionId, it.toString())
                        })
            }

            //Insert SubmissionSubmitter associations for user
            it.attach(SubmissionSubmitterDao::class.java).insert(mealSubmissionId, submitterId)

            //Insert Meal
            val mealDto = it
                    .attach(mealDaoClass)
                    .insert(mealSubmissionId, mealName, carbs, quantity, mealType = type.toString())

            //Insert all MealCuisine associations
            //TODO Make this better
            val cuisineIds = cuisineDbRepository
                    .getAllByNames(cuisines.asSequence())
                    .map { DbMealCuisineDto(mealSubmissionId, it.submission_id) }

            it.attach(MealCuisineDao::class.java).insertAll(cuisineIds.toList())

            return@inTransaction mealDto
        }
    }

    fun updateCustomMeal(
            submissionId: Int,
            submitterId: Int,
            mealName: String,
            quantity: Float,
            cuisines: Collection<String>,
            ingredients: Collection<IngredientInput>,
            type: MealType
    ): DbMealDto {
        if (ingredients.isEmpty()) {
            throw InvalidInputException("A meal must have at least an ingredient")
        }

        if (cuisines.isEmpty()) {
            throw InvalidInputException("A meal must have at least a cuisine!")
        }

        return databaseContext.inTransaction {

            submissionDbRepository.requireSubmissionOwner(submissionId, submitterId)

            submissionDbRepository.requireSubmissionType(submissionId, SubmissionType.MEAL)

            //Validate given ingredients to see if they are all in the database
            val databaseIngredients = requireIngredientsById(it, ingredients.map { it.identifier!! })

            // TODO: Logic should be isolated on the service and passed to repo as a parameter
            //Calculate meal carbs from adding each ingredient carb for given input quantity
            val carbs = databaseIngredients
                    .zip(ingredients, this::getCarbsForInputQuantity)
                    .sum()

            //Insert Meal
            val mealDto = it
                    .attach(mealDaoClass)
                    .update(submissionId, mealName, carbs, quantity, mealType = type.toString())

            //Delete existing cuisines
            it.attach(MealCuisineDao::class.java).deleteAllByMealId(submissionId)

            //Insert all MealCuisine associations
            val cuisineIds = cuisineDbRepository.getAllByNames(cuisines.asSequence())
                    .map(DbCuisineDto::submission_id)
            val mealCuisineDao = it.attach(MealCuisineDao::class.java)
            mealCuisineDao.deleteAllByMealId(submissionId)
            mealCuisineDao.insertAll(cuisineIds.map { DbMealCuisineDto(submissionId, it) }.toList())

            //Insert meal's ingredients
            val mealIngredientDao = it.attach(MealIngredientDao::class.java)
            mealIngredientDao.deleteAllByMealId(submissionId)

            //Insert meal's ingredients
            mealIngredientDao.insertAll(ingredients.map { ingredientInput ->
                DbMealIngredientDto(
                        //We know fields are not null due to validation checks
                        meal_submission_id = submissionId,
                        ingredient_submission_id = ingredientInput.identifier!!,
                        quantity = ingredientInput.quantity!!
                )
            })

            return@inTransaction mealDto
        }
    }

    /**
     * Deletes a user custom meal, if not inserted on a restaurant.
     * @throws InvalidInputException if the submitter is not the owner of the submission,
     * the submission is not a custom meal or if there is a restaurant meal that uses this custom meal.
     */
    fun deleteCustomMeal(mealId: Int, submitterId: Int) {
        return databaseContext.inTransaction {
            submissionDbRepository.requireSubmissionOwner(mealId, submitterId)
            submissionDbRepository.requireSubmissionType(mealId, SubmissionType.MEAL)

            val meal = it.attach(MealDao::class.java).getById(mealId)!!

            if(meal.meal_type != MEAL_TYPE_CUSTOM) {
                throw InvalidInputException("Can only delete custom meals!")
            }

            val restaurantMeals = it
                    .attach(RestaurantMealDao::class.java)
                    .getAllByMealId(mealId)
                    .asCachedSequence()

            if(restaurantMeals.toList().isNotEmpty()) {
                throw InvalidInputException("Cannot delete a public submission!")
            }
            submissionDbRepository.deleteSubmissionById(mealId)
        }
    }

    private fun requireIngredientsById(handle: Handle, ingredientIds: Collection<Int>): List<DbMealDto> {
        val databaseIngredients = handle
                .attach(MealDao::class.java)
                .getAllIngredientsByIds(ingredientIds)
                .toList()

        if (databaseIngredients.size != ingredientIds.size) {
            throw InvalidInputException("Not all ingredients belong to the database!")
        }
        return databaseIngredients
    }

    //TODO Remove this and use 'carbsTool'
    private fun getCarbsForInputQuantity(dbIngredientDto: DbMealDto, ingredientInput: IngredientInput): Float {
        return (ingredientInput.quantity!!.times(dbIngredientDto.carbs)).div(dbIngredientDto.amount)
    }
}