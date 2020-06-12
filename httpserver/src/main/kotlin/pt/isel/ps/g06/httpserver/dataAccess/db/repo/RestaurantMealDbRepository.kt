package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.PortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantMealDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.*
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val restaurantMealDao = RestaurantMealDao::class.java

@Repository
class RestaurantMealDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    private val contracts = listOf(REPORTABLE, VOTABLE, FAVORABLE)

    private fun getFavorite(handle: Handle, submissionId: Int, userId: Int?): Boolean? {
        return userId?.let {
            handle.attach(FavoriteDao::class.java).getByIds(submissionId, userId)
                    ?.let { true } ?: false
        }
    }

    private fun getItems(handle: Handle, dtos: Collection<DbMealDto>, userId: Int?): Collection<DbRestaurantMealItemDto> {
        return dtos.flatMap { mealDto ->
            handle.attach(RestaurantMealDao::class.java).getAllByMealId(mealDto.submission_id).map {
                Pair(mealDto, it)
            }
        }.map { pair ->
            val votesDtos = handle.attach(VotableDao::class.java).getById(pair.second.submission_id) ?: DbVotesDto(0, 0)
            val userVote = userId?.let { handle.attach(UserVoteDao::class.java).getVoteByIds(pair.second.submission_id, userId) }
            val isFavorite = userId?.let {
                handle.attach(FavoriteDao::class.java).getByIds(pair.second.submission_id, userId)?.let { true }
                        ?: false
            }
            DbRestaurantMealItemDto(
                    mealItem = DbMealItemDto(
                            meal = pair.first,
                            image = null,
                            isFavorite = isFavorite
                    ),
                    public = DbPublicDto(
                            votes = votesDtos,
                            userVote = userVote
                    ),
                    restaurantMeal = pair.second
            )
        }
    }

    fun getItemsByCuisines(cuisines: Collection<String>, userId: Int?): Collection<DbRestaurantMealItemDto> {
        return jdbi.inTransaction<Collection<DbRestaurantMealItemDto>, Exception>(isolationLevel) {
            val mealDtos = it.attach(MealDao::class.java).getAllByCuisineNames(cuisines)
            return@inTransaction getItems(it, mealDtos, userId)
        }
    }

    fun getItemsByName(mealName: String, userId: Int?): Collection<DbRestaurantMealItemDto> {
        return jdbi.inTransaction<Collection<DbRestaurantMealItemDto>, Exception>(isolationLevel) {
            val mealDtos = it.attach(MealDao::class.java).getByName(mealName)
            return@inTransaction getItems(it, mealDtos, userId)
        }
    }

    fun getRestaurantMeals(restaurantId: Int, userId: Int?): Collection<DbRestaurantMealItemDto> {
        return jdbi.inTransaction<Collection<DbRestaurantMealItemDto>, Exception>(isolationLevel) { handle ->
            val restaurantMealDtos = handle.attach(RestaurantMealDao::class.java)
                    .getAllByRestaurantId(restaurantId)
            if (restaurantMealDtos.isEmpty())
                return@inTransaction emptyList()
            return@inTransaction restaurantMealDtos.map { retaurantMealDto ->
                val mealDto = handle.attach(MealDao::class.java).getById(retaurantMealDto.meal_submission_id)!!
                val votesDtos = handle.attach(VotableDao::class.java).getById(retaurantMealDto.submission_id)
                        ?: DbVotesDto(0, 0)
                val userVote = userId?.let { handle.attach(UserVoteDao::class.java).getVoteByIds(retaurantMealDto.submission_id, userId) }
                DbRestaurantMealItemDto(
                        mealItem = DbMealItemDto(
                                meal = mealDto,
                                image = null,
                                isFavorite = getFavorite(handle, mealDto.submission_id, userId)
                        ),
                        public = DbPublicDto(
                                votes = votesDtos,
                                userVote = userVote
                        ),
                        restaurantMeal = retaurantMealDto
                )
            }
        }
    }

    private fun getMealIngredients(handle: Handle, mealDto: DbMealDto, userId: Int?): Collection<DbMealIngredientInfoDto> {
        val mealIngredientDtos = handle.attach(IngredientDao::class.java)
                .getAllByMealId(mealDto.submission_id)
                .sortedBy { it.submission_id }
        val ingredientDtos = handle.attach(MealIngredientDao::class.java)
                .getAllByMealId(mealDto.submission_id)
                .sortedBy { it.ingredient_submission_id }
        return mealIngredientDtos.zip(ingredientDtos) { ingredient, mealIngredient ->
            DbMealIngredientInfoDto(
                    //TODO replace carbs/amount/unit with real values from mealIngredient (unused)
                    ingredient = DbIngredientInfoDto(
                            name = ingredient.ingredient_name,
                            submissionId = ingredient.submission_id,
                            image = null,
                            isFavorite = getFavorite(handle, ingredient.submission_id, userId)
                    ),
                    carbs = 100,
                    amount = 100
            )
        }
    }

    fun getInfoByRestaurantMealId(restaurantMealId: Int, userId: Int): DbRestaurantMealInfoDto? {
        return jdbi.inTransaction<DbRestaurantMealInfoDto, Exception>(isolationLevel) { handle ->
            val restaurantMealDto = handle.attach(RestaurantMealDao::class.java).getById(restaurantMealId)
                    ?: return@inTransaction null
            val mealDto = handle.attach(MealDao::class.java).getById(restaurantMealDto.meal_submission_id)!!
            val cuisines = handle.attach(CuisineDao::class.java).getByMealId(restaurantMealDto.meal_submission_id)
                    .map { it.cuisine_name }
            val votesDtos = handle.attach(VotableDao::class.java).getById(restaurantMealId) ?: DbVotesDto(0, 0)
            val userVote = handle.attach(UserVoteDao::class.java).getVoteByIds(restaurantMealId, userId)
            val portionDtos = handle.attach(PortionDao::class.java).getAllByRestaurantMealId(restaurantMealId)
            return@inTransaction DbRestaurantMealInfoDto(
                    mealInfo = DbMealInfoDto(
                            mealItem = DbMealItemDto(
                                    meal = mealDto,
                                    image = null,
                                    isFavorite = getFavorite(handle, mealDto.submission_id, userId)
                            ),
                            ingredients = getMealIngredients(handle, mealDto, userId),
                            cuisines = cuisines,
                            //TODO replace carbs/amount/unit with real values
                            carbs = 100,
                            amount = 100,
                            unit = "grams"
                    ),
                    public = DbPublicDto(
                            votes = votesDtos,
                            userVote = userVote
                    ),
                    restaurantMealDto = restaurantMealDto,
                    portions = portionDtos
            )
        }
    }

    fun getInfoByRestaurantAndMealId(restaurantId: Int, mealId: Int, userId: Int): DbRestaurantMealInfoDto? {
        return jdbi.inTransaction<DbRestaurantMealInfoDto, Exception>(isolationLevel) {
            val dto = it.attach(RestaurantMealDao::class.java)
                    .getByRestaurantAndMealId(restaurantId, mealId)
                    ?: return@inTransaction null
            return@inTransaction getInfoByRestaurantMealId(dto.submission_id, userId)
        }
    }

    fun insert(
            submitterId: Int,
            mealId: Int,
            restaurantId: Int
    ): DbRestaurantMealDto {
        return jdbi.inTransaction<DbRestaurantMealDto, Exception>(isolationLevel) {
            // Check if the mealId is from a Meal
            requireSubmission(mealId, MEAL, isolationLevel)

            // Check if the restaurantId is from a Restaurant
            requireSubmission(restaurantId, RESTAURANT, isolationLevel)

            val restaurantMealDao = it.attach(restaurantMealDao)
            val existingRestaurantMeal = restaurantMealDao
                    .getByRestaurantAndMealId(restaurantId, mealId)
            if (existingRestaurantMeal != null) {
                throw InvalidInputException(InvalidInputDomain.RESTAURANT_MEAL,
                        "The restaurant with id $restaurantId already has a meal with id $mealId!"
                )
            }

            val submissionId = it.attach(SubmissionDao::class.java)
                    .insert(RESTAURANT_MEAL.toString())
                    .submission_id

            it.attach(SubmissionSubmitterDao::class.java).insert(submissionId, submitterId)

            return@inTransaction restaurantMealDao.insert(submissionId, restaurantId, mealId)
        }
    }

    fun remove(
            submitterId: Int,
            submissionId: Int
    ) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the submissionId is from a RestaurantMeal
            requireSubmission(submissionId, RESTAURANT_MEAL, isolationLevel)

            // Check if the submitter is the submission owner
            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

            // Delete all portions associated with this RestaurantMeal
            it.attach(PortionDao::class.java).getAllByRestaurantMealId(submissionId)

            // Delete restaurant meal association
            it.attach(restaurantMealDao).deleteById(submissionId)

            // Removes submission, submitter association, contracts & it's tables
            super.removeSubmission(submissionId, submitterId, RESTAURANT_MEAL, contracts, isolationLevel)
        }
    }
}