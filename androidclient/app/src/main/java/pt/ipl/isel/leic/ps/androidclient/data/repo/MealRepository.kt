package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.FavoriteOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.PortionOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.ReportOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.*
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output.OutputCuisineMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output.OutputCustomMealMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output.OutputIngredientMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output.OutputVoteMapper
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
        componentIngredientMapper = dbComponentIngredientMapper,
        componentMealMapper = dbComponentMealMapper,
        cuisinesMapper = dbCuisinesMapper,
        portionMapper = dbPortionMapper
    )
    val dbMealItemMapper = DbMealItemMapper()
    private val inputVotesMapper = InputVotesMapper()
    private val inputCuisineMapper = InputCuisineMapper()
    private val inputMealIngredientMapper = InputMealIngredientMapper()
    private val inputPortionMapper = InputPortionMapper()
    private val inputUserMapper = InputSubmissionOwnerMapper()
    private val inputMealInfoMapper = InputMealInfoMapper(
        inputVotesMapper = inputVotesMapper,
        inputCuisineMapper = inputCuisineMapper,
        inputMealIngredientMapper = inputMealIngredientMapper,
        inputPortionMapper = inputPortionMapper,
        inputUserMapper = inputUserMapper
    )
    private val inputMealItemMapper = InputMealItemMapper(
        inputVotesMapper = inputVotesMapper
    )
    private val outputVoteMapper = OutputVoteMapper()
    private val cuisineOutputMapper = OutputCuisineMapper()
    private val outputIngredientMapper = OutputIngredientMapper()
    private val outputCustomMealMapper = OutputCustomMealMapper(
        cuisineMapper = cuisineOutputMapper,
        ingredientMapper = outputIngredientMapper
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
        userSession: UserSession?,
        success: (MealInfo) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getRestaurantMeal(
            restaurantId = restaurantId,
            mealId = mealId,
            jwt = userSession?.jwt,
            success = { dtos -> success(inputMealInfoMapper.mapToModel(dtos, restaurantId)) },
            error = error
        )
    }

    fun getFavoriteMeals(
        userSession: UserSession,
        skip: Int? = 0,
        count: Int? = 30,
        cuisines: Collection<Cuisine>? = null,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getFavoriteMeals(
            jwt = userSession.jwt,
            skip = skip,
            count = count,
            cuisines = cuisines?.let(cuisineOutputMapper::mapToOutputModelCollection),
            success = { dtos -> success(inputMealItemMapper.mapToListModel(dtos)) },
            error = error
        )
    }

    fun getApiMealInfo(
        mealId: Int,
        userSession: UserSession?,
        success: (MealInfo) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getMeal(
            mealId = mealId,
            jwt = userSession?.jwt,
            success = { dtos -> success(inputMealInfoMapper.mapToModel(dtos, null)) },
            error = error
        )
    }

    fun getCustomMeals(
        userSession: UserSession,
        skip: Int? = 0,
        count: Int? = 30,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getCustomMeals(
            jwt = userSession.jwt,
            skip = skip,
            count = count,
            success = { dtos -> success(inputMealItemMapper.mapToListModel(dtos)) },
            error = error
        )
    }

    fun getMealItems(
        count: Int? = 0,
        skip: Int? = 0,
        cuisines: Collection<Cuisine>? = null,
        userSession: UserSession?,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getSuggestedMeals(
            count = count,
            skip = skip,
            cuisines = cuisines?.let(cuisineOutputMapper::mapToOutputModelCollection),
            jwt = userSession?.jwt,
            success = {
                //TODO assuming that no user filter is passed, all meals are suggested
                success(inputMealItemMapper.mapToListModel(it, null))
            },
            error = error
        )
    }

    fun getRestaurantMealItems(
        restaurantId: String,
        count: Int? = 0,
        skip: Int? = 0,
        userSession: UserSession?,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getRestaurantMeals(
            restaurantId = restaurantId,
            count = count,
            skip = skip,
            jwt = userSession?.jwt,
            success = {
                success(inputMealItemMapper.mapToListModel(it))
            },
            error = error
        )
    }

    fun addCustomMeal(
        customMeal: CustomMeal,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) = dataSource.postCustomMeal(
        customMealOutput = outputCustomMealMapper.mapToOutputModel(restaurant = customMeal),
        error = error,
        jwt = userSession.jwt
    )

    fun editCustomMeal(
        submissionId: Int,
        customMeal: CustomMeal,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) = dataSource.putMeal(
        submissionId = submissionId,
        customMealOutput = outputCustomMealMapper.mapToOutputModel(restaurant = customMeal),
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
        voteOutput = outputVoteMapper.mapToOutputModel(model = vote),
        success = success,
        error = error,
        jwt = userSession.jwt
    )

    fun putFavorite(
        restaurantId: String?,
        submissionId: Int,
        isFavorite: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        if(restaurantId == null) {
            dataSource.putMealFavorite(
                mealId = submissionId,
                favoriteOutput = FavoriteOutput(isFavorite = isFavorite),
                success = success,
                error = error,
                jwt = userSession.jwt
            )
        } else {
            dataSource.putRestaurantMealFavorite(
                restaurantId = restaurantId,
                mealId = submissionId,
                favoriteOutput = FavoriteOutput(isFavorite = isFavorite),
                success = success,
                error = error,
                jwt = userSession.jwt
            )
        }
    }

    fun addMealPortion(
        restaurantId: String?,
        mealId: Int?,
        portionOutput: PortionOutput,
        userSession: UserSession,
        onSuccess: (Int) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        dataSource.postRestaurantMealPortion(
            restaurantId = restaurantId,
            mealId = mealId,
            portionOutput = portionOutput,
            onSuccess = onSuccess,
            onError = onError,
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
            reportOutput = ReportOutput(description = reportMsg),
            success = success,
            error = error,
            jwt = userSession.jwt
        )
    }
}