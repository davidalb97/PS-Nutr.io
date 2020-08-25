package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.FavoriteOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.ReportOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.CustomRestaurantOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.VoteOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.*
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
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
        success: (DetailedRestaurantInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendPath(restaurantId)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = DetailedRestaurantInput::class.java,
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
        success: (Array<SimplifiedRestaurantInput>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendQueryParameter(LATITUDE_PARAM, latitude)
                .appendQueryParameter(LONGITUDE_PARAM, longitude)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .appendQueryNotNullListParameter(CUISINES_PARAM, cuisines)
                .build()
                .toString(),
            reqHeader = jwt?.let { buildAuthHeader(jwt) },
            dtoClass = Array<SimplifiedRestaurantInput>::class.java,
            onSuccess = success,
            onError = error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */

    fun postRestaurant(
        customRestaurantOutput: CustomRestaurantOutput,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = customRestaurantOutput,
            onError = error,
            responseConsumer = { }
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
                .authority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendPath(restaurantId)
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
                .authority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendPath(restaurantId)
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
                .authority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendPath(restaurantId)
                .appendPath(REPORT_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = reportOutput,
            onError = error,
            responseConsumer = { success() }
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */

    fun deleteRestaurant(
        restaurantId: String,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        requestParser.request(
            method = HTTPMethod.DELETE,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(RESTAURANT_PATH)
                .appendPath(restaurantId)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            onError = error,
            responseConsumer = { success() }
        )
    }
}