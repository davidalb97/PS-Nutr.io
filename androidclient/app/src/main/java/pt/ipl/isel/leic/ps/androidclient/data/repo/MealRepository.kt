package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class MealRepository(private val dataSource: MealDataSource) {

    fun getMeals(
        success: (Meal) -> Unit,
        error: () -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        /*dataSource.getById(
            { success(it) },
            { error() },
            uriParameters,
            count,
            skip
        )*/
    }

    fun getAllCustomMeals(): LiveData<List<Meal>> {
        return roomDb.mealDao().getAll()
    }

    fun insertCustomMeal(meal: Meal) {
        AsyncWorker<Unit, Unit> {
            roomDb.mealDao().insert(meal)
        }.execute()
    }
}