package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.DetailedIngredientInputDto
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.DetailedMealInputDto
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.OutputMealDto

const val MEAL_URI = "$URI_BASE/$MEAL"
const val MEAL_ID_URI = "$MEAL_URI/:id"

val INPUT_MEAL_DTO = DetailedMealInputDto::class.java
val OUTPUT_MEAL_DTO = OutputMealDto::class.java
val INPUT_MEALS_DTO = Array<DetailedMealInputDto>::class.java

class MealDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
) {

    fun getAllByRestaurantId(
        id: Int,
        success: (Array<DetailedMealInputDto>) -> Unit,
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

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getById(
        success: (DetailedMealInputDto) -> Unit,
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
        success: (OutputMealDto) -> Unit,
        error: (VolleyError) -> Unit,
        reqParameters: HashMap<String, String>?,
        ingredients: Iterable<DetailedIngredientInputDto>,
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
                OutputMealDto(
                    name = reqParameters!!["name"]!!,
                    carbs = reqParameters["carbs"]?.toInt()!!,
                    amount = reqParameters["amount"]?.toInt()!!,
                    unit = reqParameters["unit"]!!,
                    imageUrl = reqParameters["imageUrl"],
                    ingredients = ingredients
                )
            }
        )
    }

    fun postMealVote(
        success: (DetailedMealInputDto) -> Unit,
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
        success: (DetailedMealInputDto) -> Unit,
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
        success: (DetailedMealInputDto) -> Unit,
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