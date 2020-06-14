package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.*
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem

/**
 * The repository that maps every restaurant dto to its respective model
 * and sends it to the application's upper layers: view models and fragments.
 */
class RestaurantRepository(private val dataSource: RestaurantDataSource) {

    private val inputVotesMapper = InputVotesMapper()
    private val inputRestaurantItemMapper = InputRestaurantItemMapper(
        votesInputMapper = inputVotesMapper
    )
    private val inputMealInputMapper = InputMealItemMapper(inputVotesMapper)
    private val inputCuisineInputMapper = InputCuisineMapper()
    private val inputRestaurantInfoMapper = InputRestaurantInfoMapper(
        mealInputMapper = inputMealInputMapper,
        cuisineInputMapper = inputCuisineInputMapper
    )

    fun getRestaurantInfoById(
        success: (RestaurantInfo) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getById(
            { restaurantDto -> success(inputRestaurantInfoMapper.mapToModel(restaurantDto)) },
            error,
            uriParameters,
            count,
            skip
        )
    }

    fun getNearbyRestaurants(
        success: (List<RestaurantItem>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        dataSource.getNearby(
            { restaurantDtos -> success(inputRestaurantItemMapper.mapToListModel(restaurantDtos)) },
            error,
            uriParameters,
            count,
            skip
        )
    }
}