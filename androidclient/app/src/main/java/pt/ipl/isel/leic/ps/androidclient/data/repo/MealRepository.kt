package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.data.source.DataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal

class MealRepository(private val dataSource: DataSource) {

    fun getMeals(
        success: (List<Meal>) -> Unit,
        error: () -> Unit,
        count: Int,
        skip: Int
    ) {
        dataSource.getMeals(
            { success(it) },
            { error() },
            count,
            skip
        )
    }
}