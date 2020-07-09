package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.*
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

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
    private val votesInputMapper = InputVotesMapper()
    private val inputRestaurantInfoMapper = InputRestaurantInfoMapper(
        mealInputMapper = inputMealInputMapper,
        cuisineInputMapper = inputCuisineInputMapper,
        votesInputMapper = votesInputMapper
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
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        dataSource.postRestaurant(
            name,
            latitude,
            longitude,
            cuisines,
            error,
            userSession
        )
    }

    fun putVote(
        id: String,
        vote: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) = dataSource.updateVote(id, vote, success, error, userSession)
}