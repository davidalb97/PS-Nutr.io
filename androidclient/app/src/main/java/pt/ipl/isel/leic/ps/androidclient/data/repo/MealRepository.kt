package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.*
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.*
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbMealInfoRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
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

    fun getAllCustomMeals(): LiveData<List<DbMealInfoRelation>> = roomDb.mealInfoDao().getAll()

    fun insertMeal(meal: MealInfo) = AsyncWorker<Unit, Unit> {
        roomDb.mealInfoDao().insert(dbMealInfoMapper.mapToRelation(meal))
    }

    fun deleteMeal(meal: MealInfo) = AsyncWorker<Unit, Unit> {
        roomDb.mealInfoDao().delete(dbMealInfoMapper.mapToRelation(meal))
    }

    fun getMealInfo(
        isSuggested: Boolean,
        success: (MealInfo) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getById(
            { dtos -> success(inputMealInfoMapper.mapToModel(dtos, isSuggested)) },
            error,
            uriParameters,
            count,
            skip
        )
    }
}