package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.MealOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient

const val MEAL_URI = "$URI_BASE/$MEAL"
const val MEAL_ID_URI = "$MEAL_URI/:id"

val INPUT_MEAL_DTO = DetailedMealInput::class.java
val OUTPUT_MEAL_DTO = MealOutput::class.java
val INPUT_MEALS_DTO = Array<SimplifiedMealInput>::class.java

class MealDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    /*//Unused! can be obtained by get restaurant by id (RestaurantInfo has MealItems)
    fun getAllByRestaurantId(
        id: Int,
        success: (Array<SimplifiedMealInput>) -> Unit,
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
            INPUT_MEALS_DTO,
            success,
            error
        )
    }
    */

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getById(
        success: (DetailedMealInput) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_URI
        requestParser.requestAndRespond(
            Method.GET,
            uri,
            INPUT_MEAL_DTO,
            success,
            error
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */
    fun postMeal(
        success: (MealOutput) -> Unit,
        error: (VolleyError) -> Unit,
        reqParameters: HashMap<String, String>?,
        ingredients: Iterable<MealIngredient>,
        cuisines: Collection<String>,
        count: Int,
        skip: Int
    ) {
        val uri = MEAL_URI

        requestParser.requestAndRespond(
            Method.POST,
            uri,
            OUTPUT_MEAL_DTO,
            success,
            error,
            {
                MealOutput(
                    name = reqParameters!!["name"]!!,
                    quantity = reqParameters["quantity"]?.toInt()!!,
                    ingredients = ingredients,
                    cuisines = cuisines
                )
            }
        )
    }

    fun postMealVote(
        success: (DetailedMealInput) -> Unit,
        error: (VolleyError) -> Unit,
        reqParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = uriBuilder.buildUri(uri, reqParameters)

        requestParser.requestAndRespond(
            Method.POST,
            uri,
            INPUT_MEAL_DTO,
            success,
            error
        )
    }

    /**
     * ----------------------------- DELETEs ---------------------------
     */
    fun deleteMeal(
        success: (DetailedMealInput) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        val uri = uriBuilder.buildUri(MEAL_ID_URI, uriParameters)

        requestParser.requestAndRespond(
            Method.DELETE,
            uri,
            INPUT_MEAL_DTO,
            success,
            error
        )
    }

    /**
     * ----------------------------- PUTs ------------------------------
     */

    fun updateMealVote(
        success: (DetailedMealInput) -> Unit,
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
            INPUT_MEAL_DTO,
            success,
            error,
            { }
        )
    }
}