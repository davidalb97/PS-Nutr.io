package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.*
import pt.ipl.isel.leic.ps.androidclient.data.model.*

/**
 * The repository that maps every restaurant dto to its respective model
 * and sends it to the application's upper layers: view models and fragments.
 */
class RestaurantRepository(private val dataSource: RestaurantDataSource) {

    private val inputVotesMapper = InputVotesMapper()
    private val inputRestaurantItemMapper = InputRestaurantItemMapper(
        votesInputMapper = inputVotesMapper
    )
    private val inputMealInputMapper = InputMealItemMapper(
        inputVotesMapper
    )
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
        dataSource.getRestaurant(
            restaurantId = restaurantId,
            success = { restaurantDto ->
                success(inputRestaurantInfoMapper.mapToModel(restaurantDto))
            },
            error = error
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
        dataSource.getRestaurants(
            latitude = latitude,
            longitude = longitude,
            count = count,
            skip = skip,
            success = { restaurantDtos ->
                success(inputRestaurantItemMapper.mapToListModel(restaurantDtos))
            },
            error = error
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
            name = name,
            latitude = latitude,
            longitude = longitude,
            cuisines = cuisines,
            error = error,
            jwt = userSession.jwt
        )
    }

    fun putVote(
        id: String,
        vote: VoteState,
        success: () -> Unit,
        onError: (VolleyError) -> Unit,
        userSession: UserSession
    ) = dataSource.putRestaurantVote(
        restaurant = id,
        vote = vote,
        success = success,
        error = onError,
        jwt = userSession.jwt
    )

    fun putFavorite(
        restaurantId: String,
        isFavorite: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        dataSource.putRestaurantFavorite(
            restaurantId = restaurantId,
            isFavorite = isFavorite,
            success = success,
            error = error,
            jwt = userSession.jwt
        )
    }

    fun report(
        restaurantId: String,
        reportMsg: String,
        onSuccess: () -> Unit,
        onError: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        dataSource.putRestaurantReport(
            restaurant = restaurantId,
            reportStr = reportMsg,
            success = onSuccess,
            error = onError,
            jwt = userSession.jwt
        )
    }
}