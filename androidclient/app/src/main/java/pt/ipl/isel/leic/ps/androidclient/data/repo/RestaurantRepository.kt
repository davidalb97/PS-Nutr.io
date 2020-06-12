package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.InputIngredientMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.DetailedMealInputMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.SimplifiedRestaurantInputMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant

/**
 * The repository that maps every restaurant dto to its respective model
 * and sends it to the application's upper layers: view models and fragments.
 */
class RestaurantRepository(private val dataSource: RestaurantDataSource) {

    private val apiIngredientMapper = InputIngredientMapper()
    private val apiMealMapper = DetailedMealInputMapper(apiIngredientMapper)
    private val apiRestaurantMapper = SimplifiedRestaurantInputMapper(apiMealMapper)

    fun getRestaurantById(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getById(
            { restaurantDto -> success(apiRestaurantMapper.mapToModel(restaurantDto)) },
            error,
            uriParameters,
            count,
            skip
        )
    }

    fun getNearbyRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getNearby(
            { restaurantDtos -> success(apiRestaurantMapper.mapToListModel(restaurantDtos)) },
            error,
            uriParameters,
            count,
            skip
        )
    }
}