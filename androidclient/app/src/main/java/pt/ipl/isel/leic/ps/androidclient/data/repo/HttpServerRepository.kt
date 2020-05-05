package pt.ipl.isel.leic.ps.androidclient.data.repo

import android.content.Context
import com.android.volley.RequestQueue
import pt.ipl.isel.leic.ps.androidclient.data.source.ApiRequester
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

/**
 * The repository that displays to the view models all the available methods
 * to request the HTTP server.
 */
class HttpServerRepository(private val ctx: Context, private val volleyQueue: RequestQueue) {

    val apiRequester = ApiRequester(ctx, volleyQueue)

    fun getRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: () -> Unit,
        count: Int
    ) {
        apiRequester.getRestaurants(
            { success(it) },
            { error() },
            count
        )
    }

    fun getMeal(
        success: (List<Meal>) -> Unit,
        error: () -> Unit,
        count: Int
    ) {
        apiRequester.getMeals(
            { success(it) },
            { error() },
            count
        )
    }
}