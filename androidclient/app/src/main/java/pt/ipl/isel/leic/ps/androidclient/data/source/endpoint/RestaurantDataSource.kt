package pt.ipl.isel.leic.ps.androidclient.data.source.endpoint

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.source.*
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.RestaurantDto
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.RestaurantsDto
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

private const val RESTAURANT = "restaurant"

private const val RESTAURANT_ID_URI =
    "$URI_BASE/$RESTAURANT/:id"
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

val RESTAURANTS_DTO = RestaurantsDto::class.java
val RESTAURANT_DTO = RestaurantDto::class.java


class RestaurantDataSource(
    val requester: Requester
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getRestaurantById(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters!!)

        requester.httpServerRequest(
            Method.GET,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }

    fun getNearbyRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters!!)

        requester.httpServerRequest(
            Method.GET,
            uri,
            RESTAURANTS_DTO,
            success,
            error,
            null
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */

    fun postRestaurant(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters!!)

        requester.httpServerRequest(
            Method.POST,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }

    fun postRestaurantReport(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters!!)

        requester.httpServerRequest(
            Method.POST,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */

    /**
     * ----------------------------- PUTs ------------------------------
     */
}