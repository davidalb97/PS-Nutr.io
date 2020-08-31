package pt.isel.ps.g06.httpserver.dataAccess.db.repo

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

private val mealDaoClass = MealDao::class.java

@Repository
class MealDbRepository(private val databaseContext: DatabaseContext, private val cuisineDbRepository: CuisineDbRepository) {
    fun getById(mealSubmissionId: Int): DbMealDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(mealDaoClass)
                    .getById(mealSubmissionId)
        }
    }

    fun getBySubmitterId(submitterId: Int, skip: Int?, count: Int?): Sequence<DbMealDto> {
        val meals = lazy {
            databaseContext.inTransaction { handle ->
                return@inTransaction handle.attach(mealDaoClass)
                        .getAllBySubmitterIdAndType(submitterId, MealType.CUSTOM.toString(), skip, count)
            }
        }

        return Sequence { meals.value.iterator() }
    }

    fun getAllUserFavorites(submitterId: Int, count: Int?, skip: Int?): Sequence<DbMealDto> {
        val meals = lazy {
            databaseContext.inTransaction { handle ->
                return@inTransaction handle.attach(mealDaoClass)
                        .getAllUserFavorites(submitterId, count, skip)
            }
        }

        return Sequence { meals.value.iterator() }
    }

    fun getMealIngredients(mealId: Int): Sequence<DbMealIngredientDto> {
        val ingredients = lazy {
            databaseContext.inTransaction { handle ->
                return@inTransaction handle
                        .attach(MealIngredientDao::class.java)
                        .getMealIngredientsByMealId(mealId)
            }
        }

        return Sequence { ingredients.value.iterator() }
    }

    fun getMealComponents(mealId: Int): Sequence<DbMealIngredientDto> {
        val ingredients = lazy {
            databaseContext.inTransaction { handle ->
                return@inTransaction handle
                        .attach(MealIngredientDao::class.java)
                        .getMealComponentsByMealId(mealId)
            }
        }

        return Sequence { ingredients.value.iterator() }
    }

    fun getAllIngredients(skip: Int?, count: Int?): Sequence<DbMealDto> {
        val ingredients = lazy {
            databaseContext.inTransaction { handle ->
                return@inTransaction handle
                        .attach(MealDao::class.java)
                        .getAllByType(MEAL_TYPE_SUGGESTED_INGREDIENT, skip, count)
            }
        }

        return Sequence { ingredients.value.iterator() }
    }

    fun getAllSuggestedMealsByCuisineApiIds(apiSubmitterId: Int, cuisineApiIds: Sequence<String>): Sequence<DbMealDto> {
        val collection = lazy {
            databaseContext.inTransaction {
                val cuisineApiIdsList = cuisineApiIds.toList()

                if (cuisineApiIdsList.isEmpty()) return@inTransaction emptyList<DbMealDto>()

                return@inTransaction it
                        .attach(MealDao::class.java)
                        .getAllSuggestedForApiCuisines(apiSubmitterId, cuisineApiIdsList)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllSuggestedMealsFromCuisineNames(cuisineNames: Sequence<String>): Sequence<DbMealDto> {
        val collection = lazy {
            databaseContext.inTransaction { handle ->
                val cuisineNameList = cuisineNames.toList()

                if (cuisineNameList.isEmpty()) return@inTransaction emptyList<DbMealDto>()

                return@inTransaction handle
                        .attach(MealDao::class.java)
                        .getAllSuggestedForCuisineNames(cuisineNameList)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    //TODO use cuisine filtering
    fun getAllSuggestedMeals(skip: Int?, count: Int?, cuisines: Collection<String>?): Sequence<DbMealDto> {
        val collection = lazy {
            databaseContext.inTransaction { handle ->
                return@inTransaction handle
                        .attach(MealDao::class.java)
                        //.getAllSuggestedMeals(skip, count, cuisines)
                        .getAllByType(MEAL_TYPE_SUGGESTED_MEAL, skip, count)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllUserMealsForRestaurant(restaurantId: Int): Sequence<DbMealDto> {
        val collection = lazy {
            databaseContext.inTransaction { handle ->
                return@inTransaction handle
                        .attach(MealDao::class.java)
                        .getAllUserMealsByRestaurantId(restaurantId)
            }
        }
        return Sequence { collection.value.iterator() }
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
            val databaseIngredients = it
                    .attach(MealDao::class.java)
                    .getAllIngredientsByIds(ingredients.map { ingredient -> ingredient.identifier!! })

            if (databaseIngredients.size != ingredients.size) {
                throw InvalidInputException("Not all ingredients belong to the database!")
            }

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
                    .getAllByNames(cuisines.asSequence())
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


    //TODO Remove this and use 'carbsTool'
    private fun getCarbsForInputQuantity(dbIngredientDto: DbMealDto, ingredientInput: IngredientInput): Float {
        return (ingredientInput.quantity!!.times(dbIngredientDto.carbs).toFloat()).div(dbIngredientDto.amount)
    }
}