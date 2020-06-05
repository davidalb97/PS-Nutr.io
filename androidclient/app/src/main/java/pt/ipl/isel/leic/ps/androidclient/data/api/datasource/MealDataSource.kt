package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.MEAL
import pt.ipl.isel.leic.ps.androidclient.data.api.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.URI_BASE
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputMealDto
import pt.ipl.isel.leic.ps.androidclient.data.model.ApiMeal

const val MEAL_ID_URI =
    "$URI_BASE/$MEAL/:id"


//val MEALS_DTO = MealsDto::class.java
//val MEAL_DTO = MealDto::class.java

class MealDataSource(
    private val requestParser: RequestParser
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
    /*fun getById(
        success: (Meal) -> Unit,
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
            MEAL_DTO,
            success,
            error,
            null
        )
    }*/

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