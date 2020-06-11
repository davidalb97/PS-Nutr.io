package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputMealDto

const val MEAL_URI = "$URI_BASE/$MEAL"
const val MEAL_ID_URI = "$MEAL_URI/:id"

val MEAL_DTO = InputMealDto::class.java
val MEALS_DTO = Array<InputMealDto>::class.java

class MealDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    fun getAllByRestaurantId(
        id: Int,
        success: (Array<InputMealDto>) -> Unit,
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
            MEALS_DTO,
            success,
            error
        )
    }

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getById(
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
    fun postMeal(
        success: (InputMealDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.POST,
            uri,
            MEAL_DTO,
            success,
            error,
            {

            }
        )
    }

    fun postMealVote(
        success: (InputMealDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.POST,
            uri,
            MEAL_DTO,
            success,
            error,
            {

            }
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */
    fun deleteMeal(
        success: (InputMealDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.DELETE,
            uri,
            MEAL_DTO,
            success,
            error
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */

    fun updateMealVote(
        success: (InputMealDto) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = uriBuilder.buildUri(uri, uriParameters)

        requestParser.requestAndRespond(
            Method.PUT,
            uri,
            MEAL_DTO,
            success,
            error,
            { }
        )
    }
}