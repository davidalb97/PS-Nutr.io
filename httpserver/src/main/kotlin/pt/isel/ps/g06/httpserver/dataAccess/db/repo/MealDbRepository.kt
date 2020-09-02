package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.dataAccess.db.MEAL_TYPE_SUGGESTED_INGREDIENT
import pt.isel.ps.g06.httpserver.dataAccess.db.MEAL_TYPE_SUGGESTED_MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.FAVORABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealIngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.input.ingredient.IngredientInput
import java.util.stream.Stream
import kotlin.streams.toList

private val mealDaoClass = MealDao::class.java

@Repository
class MealDbRepository(private val databaseContext: DatabaseContext, private val cuisineDbRepository: CuisineDbRepository) {
    fun getById(mealSubmissionId: Int): DbMealDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getById(mealSubmissionId)
        }
    }

    fun getBySubmitterId(submitterId: Int, skip: Int?, count: Int?): Stream<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getAllBySubmitterIdAndType(submitterId, MealType.CUSTOM.toString(), skip, count)
        }

    }

    fun getAllUserFavorites(submitterId: Int, count: Int?, skip: Int?): Stream<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getAllUserFavorites(submitterId, count, skip)
        }

    }

    fun getMealIngredients(mealId: Int): Stream<DbMealIngredientDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealIngredientDao::class.java)
                    .getMealIngredientsByMealId(mealId)
        }
    }

    fun getMealComponents(mealId: Int): Stream<DbMealIngredientDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealIngredientDao::class.java)
                    .getMealComponentsByMealId(mealId)
        }

    }

    fun getAllIngredients(skip: Long?, count: Long?): Stream<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealDao::class.java)
                    .getAllByType(MEAL_TYPE_SUGGESTED_INGREDIENT, skip, count)
        }

    }

    fun getAllSuggestedMealsByCuisineApiIds(apiSubmitterId: Int, cuisineApiIds: Sequence<String>): Stream<DbMealDto> {
        val cuisineApiIdsList = cuisineApiIds.toList()
        if (cuisineApiIdsList.isEmpty()) return Stream.empty() //TODO See if eager call is possible to avoid

        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(MealDao::class.java)
                    .getAllSuggestedForApiCuisines(apiSubmitterId, cuisineApiIdsList)
        }
    }

    fun getAllSuggestedMealsFromCuisineNames(cuisineNames: Stream<String>): Stream<DbMealDto> {
        val cuisineNameList = cuisineNames.toList()
        //TODO See if eager call is possible to avoid
        if (cuisineNameList.isEmpty()) return Stream.empty()

        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealDao::class.java)
                    .getAllSuggestedForCuisineNames(cuisineNameList)
        }
    }

    //TODO use cuisine filtering
    fun getAllSuggestedMeals(skip: Long?, count: Long?, cuisines: Collection<String>?): Stream<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealDao::class.java)
                    //.getAllSuggestedMeals(skip, count, cuisines)
                    .getAllByType(MEAL_TYPE_SUGGESTED_MEAL, skip, count)
        }
    }

    fun getAllUserMealsForRestaurant(restaurantId: Int): Stream<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(MealDao::class.java)
                    .getAllUserMealsByRestaurantId(restaurantId)
        }
    }

    fun insert(
            submitterId: Int,
            mealName: String,
            quantity: Int,
            cuisines: Collection<String>,
            ingredients: Collection<IngredientInput>,
            type: MealType
    ): DbMealDto {
        if (cuisines.isEmpty()) {
            throw InvalidInputException("A meal must have at least a cuisine!")
        }

        if (ingredients.isEmpty()) {
            throw InvalidInputException("A meal must have at least one ingredient!")
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
                    .toInt()

            //Insert Meal
            val mealDto = it
                    .attach(mealDaoClass)
                    .insert(mealSubmissionId, mealName, carbs, quantity, mealType = type.toString())

            //Insert all MealCuisine associations
            //TODO Make this better
            val cuisineIds = cuisineDbRepository
                    .getAllByNames(cuisines.stream())
                    .map { DbMealCuisineDto(submitterId, it.submission_id) }

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

    fun update(
            submissionId: Int,
            submitterId: Int,
            mealName: String,
            quantity: Int,
            cuisines: Collection<String>,
            ingredients: Collection<IngredientInput>,
            type: MealType
    ): DbMealDto {
        if (cuisines.isEmpty()) {
            throw InvalidInputException("A meal must have at least a cuisine!")
        }

        if (ingredients.isEmpty()) {
            throw InvalidInputException("A meal must have at least one ingredient!")
        }

        return jdbi.inTransaction<DbMealDto, Exception>(isolationLevel) {

            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

            requireSubmission(submissionId, SubmissionType.MEAL, isolationLevel)

            //Validate given ingredients to see if they are all in the database
            val databaseIngredients = it
                    .attach(MealDao::class.java)
                    .getAllIngredientsByIds(ingredients.map { ingredient -> ingredient.identifier!! })

            if (databaseIngredients.size != ingredients.size) {
                throw InvalidInputException("Not all ingredients belong to the database!")
            }

            // TODO: Logic should be isolated on the service and passed to repo as a parameter
            //Calculate meal carbs from adding each ingredient carb for given input quantity
            val carbs = databaseIngredients
                    .zip(ingredients, this::getCarbsForInputQuantity)
                    .sum()
                    .toInt()

            //Insert Meal
            val mealDto = it
                    .attach(mealDaoClass)
                    .update(submissionId, mealName, carbs, quantity, mealType = type.toString())

            //Delete existing cuisines
            it.attach(MealCuisineDao::class.java).deleteAllByMealId(submissionId)

            //Insert all MealCuisine associations
            insertMealCuisines(it, submissionId, cuisines)

            //Insert meal's ingredients
            val mealIngredientDao = it.attach(MealIngredientDao::class.java)
            mealIngredientDao.deleteAllByMealId(submissionId)
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

    private fun insertMealCuisines(it: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        if (cuisineNames.isEmpty()) return
        val cuisineIds = cuisineDbRepository.getCuisinesByNames(cuisineNames, isolationLevel).map { it.submission_id }
        it.attach(MealCuisineDao::class.java).insertAll(cuisineIds.map { DbMealCuisineDto(submissionId, it) })
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
        return (ingredientInput.quantity!!.times(dbIngredientDto.carbs).toFloat()).div(dbIngredientDto.amount)
    }
}