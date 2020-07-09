package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.*
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbMealInfoRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.*
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class MealRepository(private val dataSource: MealDataSource) {

    val dbComponentIngredientMapper = DbComponentIngredientMapper()
    val dbComponentMealMapper = DbComponentMealMapper()
    val dbCuisinesMapper = DbCuisineMapper()
    val dbCortionMapper = DbPortionMapper()
    val dbMealInfoMapper = DbMealInfoMapper(
        dbComponentIngredientMapper,
        dbComponentMealMapper,
        dbCuisinesMapper,
        dbCortionMapper
    )
    val dbMealItemMapper = DbMealItemMapper()
    val inputVotesMapper = InputVotesMapper()
    val inputCuisineMapper = InputCuisineMapper()
    val inputMealIngredientMapper = InputMealIngredientMapper()
    val inputPortionMapper = InputPortionMapper()
    val inputMealInfoMapper = InputMealInfoMapper(
        inputVotesMapper = inputVotesMapper,
        inputCuisineMapper = inputCuisineMapper,
        inputMealIngredientMapper = inputMealIngredientMapper,
        inputPortionMapper = inputPortionMapper
    )
    val inputMealItemMapper = InputMealItemMapper(
        inputVotesMapper = inputVotesMapper
    )

    fun getByIdAndSource(dbId: Long, source: Source)
            = roomDb.mealInfoDao().getByIdAndSource(dbId, source.ordinal)

    fun getAllInfoBySource(source: Source): LiveData<List<DbMealInfoRelation>>
            = roomDb.mealInfoDao().getAllBySource(source.ordinal)

    fun getAllItemBySource(source: Source): LiveData<List<DbMealItemEntity>>
            = roomDb.mealItemDao().getAllBySource(source.ordinal)

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

    fun getApiRestaurantMealInfo(
        restaurantId: String,
        mealId: Int,
        success: (MealInfo) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getRestaurantMealById(
            restaurantId,
            mealId,
            { dtos -> success(inputMealInfoMapper.mapToModel(dtos, restaurantId)) },
            error
        )
    }

    fun getApiMealInfo(
        mealId: Int,
        success: (MealInfo) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getMealById(
            mealId,
            { dtos -> success(inputMealInfoMapper.mapToModel(dtos, null)) },
            error
        )
    }

    fun getMealItems(
        count: Int = 0,
        skip: Int = 0,
        cuisines: Collection<Cuisine>? = null,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getAll(
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
        dataSource.getAllByRestaurantId(
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
    ) =
        dataSource.postMeal(name, quantity, unit, ingredients, cuisines, error, userSession)


    fun putVote(
        restaurantId: String,
        mealId: Int,
        vote: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) = dataSource.updateVote(restaurantId, mealId, vote, success, error, userSession)
}