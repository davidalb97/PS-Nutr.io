package pt.ipl.isel.leic.ps.androidclient.data.repo

import android.content.Context
import pt.ipl.isel.leic.ps.androidclient.data.source.DataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

/**
 * The repository that displays to the view models all the available methods
 * to request the HTTP server.
 */
class HttpServerRepository(ctx: Context) {

    private val dataSource = DataSource(ctx)

    fun getRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: () -> Unit,
        count: Int,
        skip: Int
    ) {
        dataSource.getRestaurants(
            { success(it) },
            { error() },
            count,
            skip
        )
    }

    fun getMeal(
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