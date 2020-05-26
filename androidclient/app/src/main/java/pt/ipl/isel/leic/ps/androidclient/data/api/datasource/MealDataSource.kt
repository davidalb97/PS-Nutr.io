package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import pt.ipl.isel.leic.ps.androidclient.data.source.*

const val MEAL_ID_URI =
    "$URI_BASE/$MEAL/:id"


//val MEALS_DTO = MealsDto::class.java
//val MEAL_DTO = MealDto::class.java

class MealDataSource(
    private val requester: Requester
) {

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