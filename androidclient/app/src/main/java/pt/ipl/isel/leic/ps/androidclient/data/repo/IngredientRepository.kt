package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.IngredientDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.InputMealIngredientMapper
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.DbMealItemMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class IngredientRepository(private val dataSource: IngredientDataSource) {

    val dbMealItemMapper = DbMealItemMapper()
    val inputIngredientMapper = InputMealIngredientMapper()

    fun getIngredients(
        count: Int,
        skip: Int,
        success: (List<MealIngredient>) -> Unit,
        error: (Throwable) -> Unit
    ) {
        dataSource.getIngredients(
            count,
            skip,
            { success(inputIngredientMapper.mapToListModel(it)) },
            error
        )
    }

    fun insert(mealItem: MealItem) = AsyncWorker<Unit, Unit> {
        roomDb.mealItemDao().insert(dbMealItemMapper.mapToEntity(mealItem))
    }

}