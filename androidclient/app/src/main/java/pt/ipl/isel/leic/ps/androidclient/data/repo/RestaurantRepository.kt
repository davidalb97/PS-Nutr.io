package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.data.source.DataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

/**
 * The repository that displays to the view models all the available methods
 * to request the HTTP server.
 */
class RestaurantRepository(private val dataSource: DataSource) {

    fun getRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: () -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>,
        count: Int,
        skip: Int
    ) {
        dataSource.getRestaurants(
            { success(it) },
            { error() },
            uriParameters,
            count,
            skip
        )
    }
}