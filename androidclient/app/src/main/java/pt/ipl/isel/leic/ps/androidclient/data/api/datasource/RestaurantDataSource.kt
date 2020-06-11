package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.Method
import pt.ipl.isel.leic.ps.androidclient.data.api.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.URI_BASE
import pt.ipl.isel.leic.ps.androidclient.data.api.UriBuilder
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputRestaurantDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant

const val LATITUDE_VAR = ":latitude"
const val LONGITUDE_VAR = ":longitude"

private const val RESTAURANT = "restaurant"
private const val RESTAURANT_ID_URI =
    "$URI_BASE/$RESTAURANT?latitude=:latitude&longitude=:longitude"
private const val RESTAURANT_LOCATION =
    "$URI_BASE/$RESTAURANT?latitude=$LATITUDE_VAR&longitude=$LONGITUDE_VAR"
private const val RESTAURANT_REPORT =
    "$RESTAURANT_ID_URI/report"
private const val RESTAURANT_VOTE =
    "$RESTAURANT_ID_URI/vote"
private const val RESTAURANT_MEALS =
    "$RESTAURANT_ID_URI/meal"
private const val RESTAURANT_MEAL =
    "$RESTAURANT_MEALS/:mealId"
private const val RESTAURANT_MEAL_PORTION =
    "$RESTAURANT_MEAL/portion"
private const val RESTAURANT_MEAL_REPORT =
    "$RESTAURANT_MEAL/report"
private const val RESTAURANT_MEAL_VOTE =
    "$RESTAURANT_MEAL/vote"

val RESTAURANT_DTO = InputRestaurantDto::class.java


class RestaurantDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getById(
        success: (InputRestaurantDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.GET,
            uri,
            InputRestaurantDto::class.java,
            success,
            error
        )
    }

    fun getNearby(
        success: (Array<InputRestaurantDto>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_LOCATION

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.GET,
            uri,
            Array<InputRestaurantDto>::class.java,
            success,
            error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */

    fun postRestaurant(
        success: (InputRestaurantDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters:  HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.POST,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            {}
        )
    }

    fun postReport(
        success: (InputRestaurantDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters:  HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.POST,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }

    fun postVote(
        success: (InputRestaurantDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters:  HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.POST,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            {}
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */
    fun updateVote(
        success: (InputRestaurantDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters:  HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.PUT,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            {}
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */

    fun deleteVote(
        success: (InputRestaurantDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters:  HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.DELETE,
            uri,
            RESTAURANT_DTO,
            success,
            error
        )
    }
}