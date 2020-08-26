package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.FAVORABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealIngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.input.IngredientInput
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidInputException
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val mealDaoClass = MealDao::class.java

@Repository
class MealDbRepository(jdbi: Jdbi) : SubmissionDbRepository(jdbi) {
    fun getById(mealSubmissionId: Int): DbMealDto? {
        return jdbi.inTransaction<DbMealDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getById(mealSubmissionId)
        }
    }

    fun getBySubmitterId(submitterId: Int, count: Int?, skip: Int?): Sequence<DbMealDto> {
        val meals = lazy {
            jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle.attach(mealDaoClass)
                        .getAllBySubmitterIdAndType(submitterId, MealType.CUSTOM.toString(), count, skip)
            }
        }

        return Sequence { meals.value.iterator() }
    }

    fun getMealIngredients(mealId: Int): Sequence<DbMealIngredientDto> {
        val ingredients = lazy {
            jdbi.inTransaction<Collection<DbMealIngredientDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle
                        .attach(MealIngredientDao::class.java)
                        .getMealIngredients(mealId)
            }
        }

        return Sequence { ingredients.value.iterator() }
    }

    fun getMealComponents(mealId: Int): Sequence<DbMealIngredientDto> {
        val ingredients = lazy {
            jdbi.inTransaction<Collection<DbMealIngredientDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle
                        .attach(MealIngredientDao::class.java)
                        .getMealComponents(mealId)
            }
        }

        return Sequence { ingredients.value.iterator() }
    }

    fun getAllIngredients(skip: Int?, limit: Int?): Sequence<DbMealDto> {
        val ingredients = lazy {
            jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle
                        .attach(MealDao::class.java)
                        .getAllIngredients(skip, limit)
            }
        }

        return Sequence { ingredients.value.iterator() }
    }

    fun getAllSuggestedMealsByCuisineApiIds(apiSubmitterId: Int, cuisineApiIds: Sequence<String>): Sequence<DbMealDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) {
                val cuisineApiIdsList = cuisineApiIds.toList()

                if (cuisineApiIdsList.isEmpty()) return@inTransaction emptyList()

                return@inTransaction it
                        .attach(MealDao::class.java)
                        .getAllSuggestedForApiCuisines(apiSubmitterId, cuisineApiIdsList)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllSuggestedMealsFromCuisineNames(cuisineNames: Sequence<String>): Sequence<DbMealDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) { handle ->
                val cuisineNameList = cuisineNames.toList()

                if (cuisineNameList.isEmpty()) return@inTransaction emptyList()

                return@inTransaction handle
                        .attach(MealDao::class.java)
                        .getAllSuggestedForCuisineNames(cuisineNameList)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllSuggestedMeals(count: Int?, skip: Int?, cuisines: Collection<String>?): Sequence<DbMealDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle
                        .attach(MealDao::class.java)
                        .getAllSuggestedMeals(count, skip, cuisines)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllUserMealsForRestaurant(restaurantId: Int): Sequence<DbMealDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbMealDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle
                        .attach(MealDao::class.java)
                        .getAllUserMealsByRestaurantId(restaurantId)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun insert(
            submitterId: Int,
            submissionType: SubmissionType,
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
            //Validate given ingredients to see if they are all in the database
            val databaseIngredients = it
                    .attach(MealDao::class.java)
                    .getAllIngredientsByIds(ingredients.map { ingredient -> ingredient.identifier!! })

            if (databaseIngredients.size != ingredients.size) {
                throw InvalidInputException("Not all ingredients belong to the database!")
            }

            //Insert submission
            val mealSubmissionId = it.attach(SubmissionDao::class.java)
                    .insert(submissionType.toString())
                    .submission_id

            if(type == MealType.SUGGESTED) {
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
                    .insert(mealSubmissionId, mealName, carbs, quantity, type = type.toString())

            //Insert all MealCuisine associations
            insertMealCuisines(it, mealSubmissionId, cuisines)

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

    private fun insertMealCuisines(it: Handle, submissionId: Int, cuisineNames: Collection<String>) {
        if (cuisineNames.isEmpty()) return

        val cuisineIds = getCuisinesByNames(cuisineNames, isolationLevel).map { it.submission_id }
        it.attach(MealCuisineDao::class.java).insertAll(cuisineIds.map { DbMealCuisineDto(submissionId, it) })
    }


    //TODO Remove this and use 'carbsTool'
    private fun getCarbsForInputQuantity(dbIngredientDto: DbMealDto, ingredientInput: IngredientInput): Float {
        return (ingredientInput.quantity!!.times(dbIngredientDto.carbs).toFloat()).div(dbIngredientDto.amount)
    }
}