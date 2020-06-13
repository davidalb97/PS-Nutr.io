package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.InputIngredientMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.SimplifiedMealInputMapper
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.DbApiMealMapper
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.DbCustomMealMapper
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.DbFavoriteMealMapper
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.DbIngredientMapper
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbCustomMealRelation
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbFavoriteMealRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.FavoriteMeal
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class MealRepository(private val dataSource: MealDataSource) {

    val dbIngredientMapper = DbIngredientMapper()
    val dbCustomMealMapper = DbCustomMealMapper(dbIngredientMapper)
    val dbFavoriteMealMapper = DbFavoriteMealMapper(dbIngredientMapper)
    val dbApiMealMapper = DbApiMealMapper(dbIngredientMapper)
    val inputIngredientMapper = InputIngredientMapper()
    val inputMealMapper = SimplifiedMealInputMapper()

    fun getAllCustomMeals(): LiveData<List<DbCustomMealRelation>> = roomDb.customMealDao().getAll()

    fun insertCustomMeal(customMeal: CustomMeal) = AsyncWorker<Unit, Unit> {
        roomDb.customMealDao().insert(dbCustomMealMapper.mapToRelation(customMeal))
    }

    fun deleteCustomMeal(customMeal: CustomMeal) = AsyncWorker<Unit, Unit> {
        roomDb.customMealDao().delete(dbCustomMealMapper.mapToRelation(customMeal))
    }

    fun getAllFavoriteMeals(): LiveData<List<DbFavoriteMealRelation>> =
        roomDb.favoriteMealDao().getAll()

    fun insertFavoriteMeal(favoriteMeal: FavoriteMeal) = AsyncWorker<Unit, Unit> {
        roomDb.favoriteMealDao().insert(dbFavoriteMealMapper.mapToRelation(favoriteMeal))
    }

    fun deleteFavoriteMeal(favoriteMeal: FavoriteMeal) = AsyncWorker<Unit, Unit> {
        roomDb.favoriteMealDao().delete(dbFavoriteMealMapper.mapToRelation(favoriteMeal))
    }

    fun getAllApiMealsByRestaurant(
        restaurantIdentifier: Int,
        success: (List<MealItem>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getAllByRestaurantId(
            restaurantIdentifier,
            { dtos -> success(inputMealMapper.mapToListModel(dtos.toList())) },
            error,
            uriParameters,
            count,
            skip
        )
    }
    fun insertFavoriteMeal(apiMeal: MealItem) = AsyncWorker<Unit, Unit> {
        roomDb.apiMealDao().insert(dbApiMealMapper.mapToRelation(apiMeal))
    }


    fun deleteFavoriteMeal(apiMeal: MealItem) = AsyncWorker<Unit, Unit> {
        roomDb.apiMealDao().delete(dbApiMealMapper.mapToRelation(apiMeal))
    }

}