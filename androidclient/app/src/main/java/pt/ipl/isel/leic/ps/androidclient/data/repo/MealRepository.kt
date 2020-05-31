package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbCustomMealDto
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class MealRepository(private val dataSource: MealDataSource) {

    fun getAllCustomMeals(): LiveData<List<DbCustomMealDto>> {
        return roomDb.customMealDao().getAll()
    }

    fun insertCustomMeal(dbCustomMeal: DbCustomMealDto) {
        AsyncWorker<Unit, Unit> {
            roomDb.customMealDao().insert(dbCustomMeal)
        }.execute()
    }

    fun deleteCustomMeal(dbCustomMeal: DbCustomMealDto) {
        AsyncWorker<Unit, Unit> {
            roomDb.customMealDao().delete(dbCustomMeal)
        }.execute()
    }
}