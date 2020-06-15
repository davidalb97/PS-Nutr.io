package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RestaurantOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.VoteOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.repo.DEFAULT_DB_USER

private const val LATITUDE_PARAM = ":latitude"
private const val LONGITUDE_PARAM = ":longitude"
private const val RESTAURANT_ID_PARAM = ":restaurantId"
private const val MEAL_ID_PARAM = ":mealId"
private const val COUNT_PARAM = ":count"
private const val SKIP_PARAM = ":skip"
private const val SUBMITTER_QUERY = "?submitter"

private const val RESTAURANT_URI = "$URI_BASE/$RESTAURANT"
private const val RESTAURANT_ID_URI = "$RESTAURANT_URI/$RESTAURANT_ID_PARAM"
private const val RESTAURANT_LOCATION_URI = RESTAURANT_URI +
        "?latitude=$LATITUDE_PARAM" +
        "&longitude=$LONGITUDE_PARAM" +
        "&skip=$SKIP_PARAM" +
        "&count=$COUNT_PARAM"
private const val RESTAURANT_REPORT_URI = "$URI_BASE/report/$RESTAURANT_ID_PARAM"
private const val RESTAURANT_VOTE_URI = "$RESTAURANT_ID_URI/vote"
private const val RESTAURANT_MEAL_URI = "$RESTAURANT_ID_URI/$MEAL"
private const val RESTAURANT_MEAL_ID_URI = "$RESTAURANT_MEAL_URI/$MEAL_ID_PARAM"
private const val RESTAURANT_MEAL_PORTION_URI = "$RESTAURANT_MEAL_ID_URI/portion"
private const val RESTAURANT_MEAL_REPORT_URI = "$RESTAURANT_MEAL_ID_URI/report"
private const val RESTAURANT_MEAL_VOTE_URI = "$RESTAURANT_MEAL_ID_URI/vote"

val RESTAURANT_DTO = SimplifiedRestaurantInput::class.java


class RestaurantDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getInfoById(
        restaurantId: String,
        success: (DetailedRestaurantInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = RESTAURANT_ID_URI
        val params = hashMapOf(
            Pair(RESTAURANT_ID_PARAM, restaurantId)
        )
        uri = uriBuilder.buildUri(uri, params)

        requestParser.requestAndRespond(
            Method.GET,
            uri,
            DetailedRestaurantInput::class.java,
            success,
            error
        )
    }

    fun getNearby(
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
        uri = uriBuilder.buildUri(uri, params)

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
        name: String,
        latitude: Double,
        longitude: Double,
        cuisines: Iterable<Cuisine>,
        error: (VolleyError) -> Unit,
        submitterId: Int = DEFAULT_DB_USER

    ) {

        var uri = "$RESTAURANT$SUBMITTER_QUERY=$submitterId"

        requestParser.request(
            Method.POST,
            uri,
            RestaurantOutput(
                name = name,
                latitude = latitude,
                longitude = longitude,
                cuisines = cuisines.map { it.name }
            ),
            error,
            { }
        )
    }

    fun postReport(
        id: String,
        report: String,
        error: (VolleyError) -> Unit,
        submitterId: Int = DEFAULT_DB_USER
    ) {
        var uri = "$RESTAURANT_REPORT_URI$SUBMITTER_QUERY=$submitterId"

        uri =
            uriBuilder.buildUri(
                uri,
                hashMapOf(Pair(RESTAURANT_ID_PARAM, id))
            )

        /*requestParser.request(
            Method.POST,
            uri,
            RestaurantOutput(
                name = name,
                latitude = latitude,
                longitude = longitude,
                cuisines = cuisines.map { it.name }
            ),
            error,
            { }
        )*/
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */
    fun updateVote(
        restaurant: String,
        vote: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        submitterId: Int = DEFAULT_DB_USER
    ) {
        var uri = "$RESTAURANT_VOTE_URI$SUBMITTER_QUERY=$submitterId"

        uri =
            uriBuilder.buildUri(
                uri,
                hashMapOf(Pair(RESTAURANT_ID_PARAM, restaurant))
            )

        requestParser.request(
            Method.PUT,
            uri,
            VoteOutput(vote = vote),
            error,
            { success() }
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */
}