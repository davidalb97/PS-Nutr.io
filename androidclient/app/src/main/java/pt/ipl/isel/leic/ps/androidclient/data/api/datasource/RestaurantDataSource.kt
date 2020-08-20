package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.FavoriteOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.ReportOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RestaurantOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.VoteOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.HTTPMethod
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RESTAURANT
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.request.URI_BASE
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState

private const val LATITUDE_PARAM = ":latitude"
private const val LONGITUDE_PARAM = ":longitude"
private const val RESTAURANT_ID_PARAM = ":restaurantId"
private const val MEAL_ID_PARAM = ":mealId"

private const val RESTAURANT_URI = "$URI_BASE/$RESTAURANT"
private const val RESTAURANT_ID_URI = "$RESTAURANT_URI/$RESTAURANT_ID_PARAM"
private const val RESTAURANT_LOCATION_URI = RESTAURANT_URI +
        "?latitude=$LATITUDE_PARAM" +
        "&longitude=$LONGITUDE_PARAM" +
        "&skip=$SKIP_PARAM" +
        "&count=$COUNT_PARAM"
private const val RESTAURANT_ID_VOTE_URI = "$RESTAURANT_ID_URI/vote"
private const val RESTAURANT_ID_FAVORITE_URI = "$RESTAURANT_ID_URI/favorite"
private const val RESTAURANT_ID_REPORT_URI = "$RESTAURANT_ID_URI/report"


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
        var uri = RESTAURANT_ID_URI
        val params = hashMapOf(
            Pair(RESTAURANT_ID_PARAM, restaurantId)
        )
        uri = buildUri(uri, params)

        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = uri,
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
        count: Int,
        skip: Int,
        success: (Array<SimplifiedRestaurantInput>) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = RESTAURANT_LOCATION_URI
        val params = hashMapOf(
            Pair(LATITUDE_PARAM, "$latitude"),
            Pair(LONGITUDE_PARAM, "$longitude"),
            Pair(SKIP_PARAM, "$skip"),
            Pair(COUNT_PARAM, "$count")
        )
        uri = buildUri(uri, params)

        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = uri,
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
        name: String,
        latitude: Double,
        longitude: Double,
        cuisines: Iterable<Cuisine>,
        error: (VolleyError) -> Unit,
        jwt: String

    ) {
        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.POST,
            uri = RESTAURANT,
            reqHeader = reqHeader,
            reqPayload = RestaurantOutput(
                name = name,
                latitude = latitude,
                longitude = longitude,
                cuisines = cuisines.map { it.name }
            ),
            onError = error,
            responseConsumer = { }
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */
    fun putRestaurantVote(
        restaurant: String,
        vote: VoteState,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(RESTAURANT_ID_VOTE_URI, hashMapOf(Pair(RESTAURANT_ID_PARAM, restaurant)))

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.PUT,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = VoteOutput(
                vote = vote
            ),
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantFavorite(
        restaurantId: String,
        isFavorite: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(
            RESTAURANT_ID_FAVORITE_URI,
            hashMapOf(Pair(RESTAURANT_ID_PARAM, restaurantId))
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.PUT,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = FavoriteOutput(isFavorite = isFavorite),
            onError = error,
            responseConsumer = { success() }
        )
    }

    fun putRestaurantReport(
        restaurant: String,
        reportStr: String,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        jwt: String
    ) {
        val uri = buildUri(
            RESTAURANT_ID_REPORT_URI,
            hashMapOf(Pair(RESTAURANT_ID_PARAM, restaurant))
        )

        // Composing the authorization header
        val reqHeader = buildAuthHeader(jwt)

        requestParser.request(
            method = HTTPMethod.PUT,
            uri = uri,
            reqHeader = reqHeader,
            reqPayload = ReportOutput(description = reportStr),
            onError = error,
            responseConsumer = { success() }
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */
}