package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.source.DataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

/**
 * The repository that displays to the view models all the available methods
 * to request the HTTP server.
 */
class RestaurantRepository(private val dataSource: DataSource) {

    fun getRestaurantById(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getRestaurantById(
            success,
            error,
            uriParameters,
            count,
            skip
        )
    }

    fun getNearbyRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getNearbyRestaurants(
            success,
            error,
            uriParameters,
            count,
            skip
        )
    }
}