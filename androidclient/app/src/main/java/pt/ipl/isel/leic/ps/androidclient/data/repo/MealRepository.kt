package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.data.source.endpoint.MealDataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal

class MealRepository(private val dataSource: MealDataSource) {

    fun getMeals(
        success: (Meal) -> Unit,
        error: () -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getById(
            { success(it) },
            { error() },
            uriParameters,
            count,
            skip
        )
    }
}