package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.*
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbMealInfoRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.*
import pt.ipl.isel.leic.ps.androidclient.util.AsyncWorker

class MealRepository(private val dataSource: MealDataSource) {

    private val dbComponentIngredientMapper = DbComponentIngredientMapper()
    private val dbComponentMealMapper = DbComponentMealMapper()
    private val dbCuisinesMapper = DbCuisineMapper()
    private val dbPortionMapper = DbPortionMapper()
    val dbMealInfoMapper = DbMealInfoMapper(
        dbComponentIngredientMapper,
        dbComponentMealMapper,
        dbCuisinesMapper,
        dbPortionMapper
    )
    val dbMealItemMapper = DbMealItemMapper()
    private val inputVotesMapper = InputVotesMapper()
    private val inputCuisineMapper = InputCuisineMapper()
    private val inputMealIngredientMapper = InputMealIngredientMapper()
    private val inputPortionMapper = InputPortionMapper()
    private val inputMealInfoMapper = InputMealInfoMapper(
            inputVotesMapper = inputVotesMapper,
            inputCuisineMapper = inputCuisineMapper,
            inputMealIngredientMapper = inputMealIngredientMapper,
            inputPortionMapper = inputPortionMapper
        )
    private val inputMealItemMapper = InputMealItemMapper(
        inputVotesMapper = inputVotesMapper
    )

    fun getByIdAndSource(dbId: Long, source: Source) =
        roomDb.mealInfoDao().getByIdAndSource(dbId, source.ordinal)

    fun getAllInfoBySource(source: Source): LiveData<List<DbMealInfoRelation>> =
        roomDb.mealInfoDao().getAllBySource(source.ordinal)

    fun getAllItemBySource(source: Source): LiveData<List<DbMealItemEntity>> =
        roomDb.mealItemDao().getAllBySource(source.ordinal)

    fun insertInfo(meal: MealInfo) = AsyncWorker<Unit, Unit> {
        roomDb.mealInfoDao().insert(dbMealInfoMapper.mapToRelation(meal))
    }

    fun insertItem(meal: MealItem) = AsyncWorker<Unit, Unit> {
        roomDb.mealItemDao().insert(dbMealItemMapper.mapToEntity(meal))
    }

    fun deleteInfo(meal: MealInfo) = AsyncWorker<Unit, Unit> {
        roomDb.mealInfoDao().delete(dbMealInfoMapper.mapToRelation(meal))
    }

    fun deleteInfoById(dbMealId: Long) = AsyncWorker<Unit, Unit> {
        roomDb.mealInfoDao().deleteById(dbMealId)
    }

    fun deleteItem(mealItem: MealItem) = AsyncWorker<Unit, Unit> {
        roomDb.mealItemDao().delete(dbMealItemMapper.mapToEntity(mealItem))
    }

    fun deleteItemById(dbMealId: Long) = AsyncWorker<Unit, Unit> {
        roomDb.mealItemDao().deleteById(dbMealId)
    }

    fun getApiRestaurantMealInfo(
        restaurantId: String,
        mealId: Int,
        success: (MealInfo) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getRestaurantMeal(
            restaurantId = restaurantId,
            mealId = mealId,
            success = { dtos -> success(inputMealInfoMapper.mapToModel(dtos, restaurantId)) },
            error = error
        )
    }

    fun getFavoriteMeals(
        userSession: UserSession,
        skip: Int = 0,
        count: Int = 30,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getFavoriteMeals(
            jwt = userSession.jwt,
            skip = skip,
            count = count,
            success = { dtos -> success(inputMealItemMapper.mapToListModel(dtos)) },
            error = error
        )
    }

    fun getApiMealInfo(
        mealId: Int,
        success: (MealInfo) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getMeal(
            mealId = mealId,
            success = { dtos -> success(inputMealInfoMapper.mapToModel(dtos, null)) },
            error = error
        )
    }

    fun getMealItems(
        count: Int = 0,
        skip: Int = 0,
        cuisines: Collection<Cuisine>? = null,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getMeals(
            count = count,
            skip = skip,
            cuisines = cuisines,
            success = {
                //TODO assuming that no user filter is passed, all meals are suggested
                success(inputMealItemMapper.mapToListModel(it, null))
            },
            error = error
        )
    }

    fun getRestaurantMealItems(
        restaurantId: String,
        count: Int = 0,
        skip: Int = 0,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getRestaurantMeals(
            restaurantId = restaurantId,
            count = count,
            skip = skip,
            success = {
                success(inputMealItemMapper.mapToListModel(it))
            },
            error = error
        )
    }

    fun postMeal(
        name: String,
        quantity: Int,
        unit: String,
        ingredients: Iterable<MealIngredient>,
        cuisines: Iterable<Cuisine>,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) = dataSource.postMeal(
        name = name,
        quantity = quantity,
        unit = unit,
        ingredients = ingredients,
        cuisines = cuisines,
        error = error,
        jwt = userSession.jwt
    )


    fun putVote(
        restaurantId: String,
        mealId: Int,
        vote: VoteState,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) = dataSource.putRestaurantMealVote(
        restaurantId = restaurantId,
        mealId = mealId,
        vote = vote,
        success = success,
        error = error,
        jwt = userSession.jwt
    )

    fun putFavorite(
        restaurantId: String,
        submissionId: Int,
        isFavorite: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        dataSource.putRestaurantMealFavorite(
            restaurantId = restaurantId,
            mealId = submissionId,
            isFavorite = isFavorite,
            success = success,
            error = error,
            jwt = userSession.jwt
        )
    }

    fun report(
        mealId: Int,
        restaurantId: String,
        reportMsg: String,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        dataSource.putRestaurantMealReport(
            restaurantId = restaurantId,
            mealId = mealId,
            reportStr = reportMsg,
            success = success,
            error = error,
            jwt = userSession.jwt
        )
    }
}