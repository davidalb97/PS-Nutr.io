package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.ApiRestaurantDto
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.ApiRestaurantMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant

/**
 * The repository that displays to the view models all the available methods
 * to request the HTTP server.
 */
class RestaurantRepository(private val dataSource: RestaurantDataSource) {

    val apiMapper = ApiRestaurantMapper()

    fun getRestaurantById(
        success: (ApiRestaurantDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
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
        success: (Array<ApiRestaurantDto>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
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