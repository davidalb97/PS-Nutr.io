package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class MealRepository(private val dataSource: MealDataSource) {

    fun getAllCustomMeals(): LiveData<List<CustomMealDto>> {
        return roomDb.customMealDao().getAll()
    }

    fun insertCustomMeal(customMeal: CustomMealDto) {
        AsyncWorker<Unit, Unit> {
            roomDb.customMealDao().insert(customMeal)
        }.execute()
    }

    fun deleteCustomMeal(customMeal: CustomMealDto) {
        AsyncWorker<Unit, Unit> {
            roomDb.customMealDao().delete(customMeal)
        }.execute()
    }
}