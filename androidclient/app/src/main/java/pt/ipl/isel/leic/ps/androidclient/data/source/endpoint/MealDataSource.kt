package pt.ipl.isel.leic.ps.androidclient.data.source.endpoint

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.source.*
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.MealsDto
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal

const val MEAL_ID_URI =
    "$URI_BASE/$MEAL/:id"

val MEALS_DTO = MealsDto::class.java

class MealDataSource(
    private val requester: Requester
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getMeals(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri = requester.buildUri(uri, uriParameters!!)

        requester.httpServerRequest(
            Method.GET,
            uri,
            MEALS_DTO,
            success,
            error,
            null
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */

    /**
     * ----------------------------- DELETEs ---------------------------
     */

    /**
     * ----------------------------- PUTs ------------------------------
     */
}