package pt.ipl.isel.leic.ps.androidclient.data.source.endpoint

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.source.*
import pt.ipl.isel.leic.ps.androidclient.data.source.dto.RestaurantDto
import pt.ipl.isel.leic.ps.androidclient.data.source.mapper.RestaurantMapper
import pt.ipl.isel.leic.ps.androidclient.data.source.mapper.RestaurantsMapper
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

private const val RESTAURANT = "restaurant"

private const val RESTAURANT_ID_URI =
    "$URI_BASE/$RESTAURANT?latitude=38.736946&longitude=-9.142685"
private const val RESTAURANT_LOCATION =
    "$URI_BASE/$RESTAURANT?latitude=:latitude&longitude=:longitude"
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

val RESTAURANT_DTO = RestaurantDto::class.java


class RestaurantDataSource(
    val requester: Requester
) {

    private val restaurantMapper =
        RestaurantMapper()
    private val restaurantsMapper =
        RestaurantsMapper(
            restaurantMapper
        )

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getById(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
         Method.GET,
            uri,
            RestaurantDto::class.java,
            restaurantMapper::map,
            success,
            error,
            null
        )
    }

    fun getNearby(
        success: (List<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        //uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.GET,
            uri,
            Array<RestaurantDto>::class.java,
            restaurantsMapper::map,
            success,
            error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */

    /*fun postRestaurant(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.POST,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }*/

    /*fun postReport(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.POST,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }*/

    /*fun postVote(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.POST,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }*/

    /**
     * ----------------------------- PUTs ------------------------------
     */
    /*fun updateVote(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.PUT,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }*/

    /**
     * ----------------------------- DELETEs ---------------------------
     */

    /*fun deleteVote(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            RESTAURANT_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.DELETE,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }*/

}