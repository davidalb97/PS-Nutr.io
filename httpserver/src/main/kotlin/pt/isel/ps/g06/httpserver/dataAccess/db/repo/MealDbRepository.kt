package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.FAVORABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealIngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.input.IngredientInput
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import java.util.stream.Stream
import kotlin.streams.toList

@Repository
class MealDbRepository(val databaseContext: DatabaseContext) {
    fun getById(mealSubmissionId: Int): DbMealDto? {
        return databaseContext.inTransaction { handle -> handle.attach(MealDao::class.java).getById(mealSubmissionId) }
    }

    //TODO Should be DbMealDto?
    fun getMealIngredients(mealId: Int): Stream<DbMealIngredientDto> {
        return databaseContext.inTransaction { handle ->
            handle.attach(MealIngredientDao::class.java).getMealIngredients(mealId)
        }
    }

    fun getMealComponents(mealId: Int): Stream<DbMealIngredientDto> {
        return databaseContext.inTransaction { handle ->
            handle.attach(MealIngredientDao::class.java).getMealComponents(mealId)
        }
    }

    fun getAllIngredients(skip: Int?, limit: Int?): Stream<DbMealDto> {
        return databaseContext.inTransaction { handle ->
            handle.attach(MealDao::class.java).getAllIngredients()
        }
    }

    fun getAllSuggestedMealsByCuisineApiIds(apiSubmitterId: Int, cuisineApiIds: Collection<String>): Stream<DbMealDto> {
        return databaseContext.inTransaction {
            if (cuisineApiIds.isEmpty()) return@inTransaction Stream.empty()
            it.attach(MealDao::class.java).getAllSuggestedForApiCuisines(apiSubmitterId, cuisineApiIds)
        }
    }


    fun getAllSuggestedMealsFromCuisineNames(cuisineNames: Collection<String>): Stream<DbMealDto> {
        return databaseContext.inTransaction {
            if (cuisineNames.isEmpty()) return@inTransaction Stream.empty()
            it.attach(MealDao::class.java).getAllSuggestedForCuisineNames(cuisineNames)
        }
    }

    fun getAllSuggestedMeals(): Stream<DbMealDto> {
        return databaseContext.inTransaction {
            it.attach(MealDao::class.java).getAllSuggestedMeals()
        }
    }

    fun getAllUserMealsForRestaurant(restaurantId: Int): Stream<DbMealDto> {
        return databaseContext.inTransaction {
            it.attach(MealDao::class.java).getAllUserMealsByRestaurantId(restaurantId)
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
            quantity: Int,
            cuisines: Collection<String>,
            ingredients: Collection<IngredientInput>
    ): DbMealDto {
        //TODO This _should_ receive a Meal as parameter
        if (cuisines.isEmpty()) {
            throw InvalidInputException(InvalidInputDomain.CUISINE, "A meal must have at least a cuisine!")
        }

        if (ingredients.isEmpty()) {
            throw InvalidInputException(InvalidInputDomain.INGREDIENT, "A meal must have at least one ingredient!")
        }

        return databaseContext.inTransaction { handle ->
            //Validate given ingredients to see if they are all in the database
            //TODO Check if a lazy way can be done instead of checking for size
            val databaseIngredients = handle
                    .attach(MealDao::class.java)
                    .getAllIngredientsByIds(ingredients.map { ingredient -> ingredient.identifier!! })
                    .toList()

            if (databaseIngredients.size != ingredients.size) {
                throw InvalidInputException(
                        InvalidInputDomain.INGREDIENT,
                        "Not all ingredients belong to the database!"
                )
            }

            //Insert submission
            val mealSubmissionId = handle.attach(SubmissionDao::class.java)
                    .insert(MEAL.toString())
                    .submission_id

            //Insert contract FAVORABLE
            handle.attach(SubmissionContractDao::class.java)
                    .insertAll(mutableListOf(FAVORABLE).map {
                        SubmissionContractParam(mealSubmissionId, it.toString())
                    })

            //Insert SubmissionSubmitter associations for user
            handle.attach(SubmissionSubmitterDao::class.java).insert(mealSubmissionId, submitterId)


            //Calculate meal carbs from adding each ingredient carb for given input quantity
            val carbs = databaseIngredients
                    .zip(ingredients, this::getCarbsForInputQuantity)
                    .sum()
                    .toInt()

            //Insert Meal
            val mealDto = handle
                    .attach(MealDao::class.java)
                    .insert(mealSubmissionId, mealName, carbs, quantity)

            //Insert all MealCuisine associations
            //TODO Fix/remove
//            insertMealCuisines(handle, mealSubmissionId, cuisines)

            //Insert meal's ingredients
            handle.attach(MealIngredientDao::class.java).insertAll(ingredients.map { ingredientInput ->
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

//    private fun insertMealCuisines(it: Handle, submissionId: Int, cuisineNames: Collection<String>) {
//        val cuisineIds = getCuisinesByNames(cuisineNames, isolationLevel).map { it.submission_id }
//        it.attach(MealCuisineDao::class.java).insertAll(cuisineIds.map { DbMealCuisineDto(submissionId, it) })
//    }


    //TODO Remove this and use 'carbsTool'
    private fun getCarbsForInputQuantity(dbIngredientDto: DbMealDto, ingredientInput: IngredientInput): Float {
        return (ingredientInput.quantity!!.times(dbIngredientDto.carbs).toFloat()).div(dbIngredientDto.amount)
    }
}