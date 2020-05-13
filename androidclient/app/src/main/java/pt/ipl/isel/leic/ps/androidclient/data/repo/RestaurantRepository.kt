package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.source.endpoint.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

/**
 * The repository that displays to the view models all the available methods
 * to request the HTTP server.
 */
class RestaurantRepository(private val dataSource: RestaurantDataSource) {

    fun getRestaurantById(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getById(
            success,
            error,
            uriParameters,
            count,
            skip
        )
    }

    fun getNearbyRestaurants(
        success: (Array<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getNearby(
            success,
            error,
            uriParameters,
            count,
            skip
        )
    }
}