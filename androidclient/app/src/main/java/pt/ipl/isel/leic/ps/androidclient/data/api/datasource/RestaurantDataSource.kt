package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.Method
import pt.ipl.isel.leic.ps.androidclient.data.api.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.URI_BASE
import pt.ipl.isel.leic.ps.androidclient.data.api.UriBuilder
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RestaurantMealOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RestaurantOutput

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

val SIMPLE_RESTAURANT_INPUT_DTO = SimplifiedRestaurantInput::class.java
val RESTAURANT_OUTPUT_DTO = RestaurantOutput::class.java


class RestaurantDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getById(
        success: (DetailedRestaurantInput) -> Unit,
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
            DetailedRestaurantInput::class.java,
            success,
            error
        )
    }

    fun getNearby(
        success: (Array<SimplifiedRestaurantInput>) -> Unit,
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
            Array<SimplifiedRestaurantInput>::class.java,
            success,
            error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */

    fun postRestaurant(
        success: (RestaurantOutput) -> Unit,
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
            RESTAURANT_OUTPUT_DTO,
            success,
            error,
            {}
        )
    }

    fun postReport(
        success: (RestaurantOutput) -> Unit,
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
            RESTAURANT_OUTPUT_DTO,
            success,
            error,
            null
        )
    }

    fun postVote(
        success: (SimplifiedRestaurantInput) -> Unit,
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
            SIMPLE_RESTAURANT_INPUT_DTO,
            success,
            error,
            {}
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */
    fun updateVote(
        success: (SimplifiedRestaurantInput) -> Unit,
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
            SIMPLE_RESTAURANT_INPUT_DTO,
            success,
            error,
            {}
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */

    fun deleteVote(
        success: (SimplifiedRestaurantInput) -> Unit,
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
            SIMPLE_RESTAURANT_INPUT_DTO,
            success,
            error
        )
    }
}