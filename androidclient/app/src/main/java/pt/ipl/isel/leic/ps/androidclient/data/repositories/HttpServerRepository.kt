package pt.ipl.isel.leic.ps.androidclient.data.repositories

import com.android.volley.RequestQueue
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.data.sources.ApiRequester
import pt.ipl.isel.leic.ps.androidclient.data.sources.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.sources.model.Restaurant

class HttpServerRepository {

    private val apiRequester = ApiRequester(NutrioApp.app)
    private val volleyQueue: RequestQueue = NutrioApp.requestQueue

    // TODO
    fun getRestaraunts(
        success: (List<Restaurant>) -> Unit,
        error: () -> Unit,
        count: Int
    ) {
        val request = apiRequester
            .getRestaurants(
                { },
                { error() },
                count
            )

        volleyQueue.add(request)
    }

    // TODO
    fun getMeal(
        success: (List<Meal>) -> Unit,
        error: () -> Unit,
        count: Int
    ) {
        val request = apiRequester
            .getMeals(
                { },
                { error() },
                count
            )

        volleyQueue.add(request)
    }
}