package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurant.RestaurantInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurant.RestaurantItemContainerInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.CustomRestaurantOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.FavoriteOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.ReportOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.VoteOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.HTTPMethod
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.util.appendQueryNotNullListParameter
import pt.ipl.isel.leic.ps.androidclient.data.util.appendQueryNotNullParameter
import pt.ipl.isel.leic.ps.androidclient.data.util.appendQueryParameter

private const val LATITUDE_PARAM = "latitude"
private const val LONGITUDE_PARAM = "longitude"


class RestaurantDataSource(
    private val requestParser: RequestParser
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getRestaurant(
        jwt: String?,
        restaurantId: String,
        success: (RestaurantInfoInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(API_PATH)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = RestaurantInfoInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getRestaurants(
        jwt: String?,
        latitude: Double,
        longitude: Double,
        count: Int?,
        skip: Int?,
        cuisines: Collection<String>?,
        success: (RestaurantItemContainerInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(API_PATH)
                .appendPath(RESTAURANT_PATH)
                .appendQueryParameter(LATITUDE_PARAM, latitude)
                .appendQueryParameter(LONGITUDE_PARAM, longitude)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .appendQueryNotNullListParameter(CUISINES_PARAM, cuisines)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = RestaurantItemContainerInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    fun getFavoriteRestaurants(
        jwt: String?,
        count: Int?,
        skip: Int?,
        success: (RestaurantItemContainerInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(API_PATH)
                .appendPath(USER_PATH)
                .appendPath(FAVORITE_PATH)
                .appendPath(RESTAURANT_PATH)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = RestaurantItemContainerInput::class.java,
            onSuccess = success,
            onError = error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */

    fun postRestaurant(
        customRestaurantOutput: CustomRestaurantOutput,
        onSuccess: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(API_PATH)
                .appendPath(RESTAURANT_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = customRestaurantOutput,
            onError = error,
            responseConsumer = { onSuccess() }
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */
    fun putRestaurantVote(
        restaurantId: String,
        voteOutput: VoteOutput,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(API_PATH)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(VOTE_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = voteOutput,
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantFavorite(
        restaurantId: String,
        favoriteOutput: FavoriteOutput,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(API_PATH)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(FAVORITE_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = favoriteOutput,
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantReport(
        restaurantId: String,
        reportOutput: ReportOutput,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.PUT,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(API_PATH)
                .appendPath(RESTAURANT_PATH)
                .appendEncodedPath(restaurantId)
                .appendPath(REPORT_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = reportOutput,
            onError = error,
            responseConsumer = { success() }
        )
    }
}