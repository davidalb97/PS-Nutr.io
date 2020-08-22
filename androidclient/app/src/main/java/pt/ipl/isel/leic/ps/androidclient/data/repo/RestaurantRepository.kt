package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.RestaurantDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.*
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output.*
import pt.ipl.isel.leic.ps.androidclient.data.model.*

/**
 * The repository that maps every restaurant dto to its respective model
 * and sends it to the application's upper layers: view models and fragments.
 */
class RestaurantRepository(private val dataSource: RestaurantDataSource) {

    private val inputVotesMapper = InputVotesMapper()
    private val inputRestaurantItemMapper = InputRestaurantItemMapper(inputVotesMapper)
    private val inputMealInputMapper = InputMealItemMapper(inputVotesMapper)
    private val inputCuisineInputMapper = InputCuisineMapper()
    private val votesInputMapper = InputVotesMapper()
    private val inputRestaurantInfoMapper = InputRestaurantInfoMapper(
        mealInputMapper = inputMealInputMapper,
        cuisineInputMapper = inputCuisineInputMapper,
        votesInputMapper = votesInputMapper
    )
    private val favoriteOutputMapper = OutputFavoriteMapper()
    private val voteOutputMapper = OutputVoteMapper()
    private val reportOutputMapper = OutputReportMapper()
    private val cuisineOutputMapper = OutputCuisineMapper()
    private val customRestaurantOutputMapper =
        OutputCustomRestaurantMapper(cuisineMapper = cuisineOutputMapper)

    fun getRestaurantInfoById(
        restaurantId: String,
        userSession: UserSession?,
        success: (RestaurantInfo) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getRestaurant(
            restaurantId = restaurantId,
            jwt = userSession?.jwt,
            success = { restaurantDto ->
                success(inputRestaurantInfoMapper.mapToModel(restaurantDto))
            },
            error = error
        )
    }

    fun getNearbyRestaurants(
        latitude: Double,
        longitude: Double,
        cuisines: Collection<Cuisine>?,
        count: Int?,
        skip: Int?,
        userSession: UserSession?,
        success: (List<RestaurantItem>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        dataSource.getRestaurants(
            latitude = latitude,
            longitude = longitude,
            cuisines = cuisines,
            count = count,
            skip = skip,
            jwt = userSession?.jwt,
            success = { restaurantDtos ->
                success(inputRestaurantItemMapper.mapToListModel(restaurantDtos))
            },
            error = error
        )
    }

    fun addCustomRestaurant(
        customRestaurant: CustomRestaurant,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        dataSource.postRestaurant(
            customRestaurantOutput = customRestaurantOutputMapper.mapToOutputModel(
                restaurant = customRestaurant
            ),
            error = error,
            jwt = userSession.jwt
        )
    }

    fun changeVote(
        id: String,
        vote: VoteState,
        success: () -> Unit,
        onError: (VolleyError) -> Unit,
        userSession: UserSession
    ) = dataSource.putRestaurantVote(
        restaurantId = id,
        voteOutput = voteOutputMapper.mapToOutputModel(vote),
        success = success,
        error = onError,
        jwt = userSession.jwt
    )

    fun changeFavorite(
        restaurantId: String,
        isFavorite: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        dataSource.putRestaurantFavorite(
            restaurantId = restaurantId,
            favoriteOutput = favoriteOutputMapper.mapToOutputModel(isFavorite),
            success = success,
            error = error,
            jwt = userSession.jwt
        )
    }

    fun addReport(
        restaurantId: String,
        reportMsg: String,
        onSuccess: () -> Unit,
        onError: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        dataSource.putRestaurantReport(
            restaurantId = restaurantId,
            reportOutput = reportOutputMapper.mapToOutputModel(reportMsg),
            success = onSuccess,
            error = onError,
            jwt = userSession.jwt
        )
    }
}