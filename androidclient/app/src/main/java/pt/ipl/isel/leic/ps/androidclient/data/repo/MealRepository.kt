package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.CustomMealMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class MealRepository(private val dataSource: MealDataSource) {

    val customMealMapper = CustomMealMapper()

    fun getAllCustomMeals(): LiveData<List<CustomMeal>> =
        customMealMapper.mapToListModel(roomDb.customMealDao().getAll())


    fun insertCustomMeal(dbCustomMeal: DbCustomMeal): AsyncWorker<Unit, Unit> =
        AsyncWorker<Unit, Unit> {
            roomDb.customMealDao().insert(dbCustomMeal)
        }


    fun deleteCustomMeal(dbCustomMeal: DbCustomMeal): AsyncWorker<Unit, Unit> =
        AsyncWorker<Unit, Unit> {
            roomDb.customMealDao().delete(dbCustomMeal)
        }
}