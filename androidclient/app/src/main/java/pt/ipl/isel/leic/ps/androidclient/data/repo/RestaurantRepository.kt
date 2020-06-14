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
        restaurantId: String,
        success: (RestaurantInfo) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getInfoById(
            restaurantId,
            { restaurantDto -> success(inputRestaurantInfoMapper.mapToModel(restaurantDto)) },
            error
        )
    }

    fun getNearbyRestaurants(
        latitude: Double,
        longitude: Double,
        count: Int,
        skip: Int,
        success: (List<RestaurantItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getNearby(
            latitude,
            longitude,
            count,
            skip,
            { restaurantDtos -> success(inputRestaurantItemMapper.mapToListModel(restaurantDtos)) },
            error
        )
    }
}