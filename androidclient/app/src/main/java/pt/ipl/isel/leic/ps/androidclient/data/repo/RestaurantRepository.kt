package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.*
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
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

    fun postRestaurant(
        name: String,
        latitude: Double,
        longitude: Double,
        cuisines: Iterable<Cuisine>,
        error: (VolleyError) -> Unit
    ) {
        dataSource.postRestaurant(
            name,
            latitude,
            longitude,
            cuisines,
            error
        )
    }

    fun postVote(
        id: String,
        vote: Boolean,
        error: (VolleyError) -> Unit
    ) = dataSource.postVote(id, vote, error)

    fun updateVote(
        id: String,
        vote: Boolean,
        error: (VolleyError) -> Unit
    ) = dataSource.updateVote(id, vote, error)

    fun deleteVote(
        id: String,
        vote: Boolean,
        error: (VolleyError) -> Unit
    ) = dataSource.deleteVote(id, error)
}