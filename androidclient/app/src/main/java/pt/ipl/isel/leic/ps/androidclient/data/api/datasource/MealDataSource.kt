package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputMealDto

const val MEAL_ID_URI =
    "$URI_BASE/$MEAL/:id"


val MEAL_DTO = InputMealDto::class.java

class MealDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    fun getAllByRestaurantIdentifier(
        identifier: Int,
        success: (List<InputMealDto>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        TODO()
    }

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getByName(
        success: (InputMealDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.GET,
            uri,
            MEAL_DTO,
            success,
            error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */
    /*fun postMeal(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.GET,
            uri,
            MEALS_DTO,
            success,
            error,
            null
        )
    }*/

    /*fun postMealVote(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.GET,
            uri,
            MEALS_DTO,
            success,
            error,
            null
        )
    }*/

    /**
     * ----------------------------- DELETEs ---------------------------
     */
    /*fun deleteMeal(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.GET,
            uri,
            MEALS_DTO,
            success,
            error,
            null
        )
    }*/

    /**
     * ----------------------------- PUTs ------------------------------
     */

    /*fun updateMealVote(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = requester.buildUri(uri, uriParameters)

        requester.httpServerRequest(
            Method.GET,
            uri,
            MEALS_DTO,
            success,
            error,
            null
        )
    }*/
}