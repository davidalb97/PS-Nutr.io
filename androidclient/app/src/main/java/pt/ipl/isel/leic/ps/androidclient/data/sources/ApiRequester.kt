package pt.ipl.isel.leic.ps.androidclient.data.sources

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.json.JSONObject
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.data.sources.dtos.IUnDto
import pt.ipl.isel.leic.ps.androidclient.data.sources.dtos.MealsDto
import pt.ipl.isel.leic.ps.androidclient.data.sources.dtos.RestaurantsDto
import pt.ipl.isel.leic.ps.androidclient.data.sources.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.sources.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

const val ADDRESS = "TODO"
const val PORT = "TODO"
const val GET = Request.Method.GET
const val POST = Request.Method.POST
const val DELETE = Request.Method.DELETE
const val PUT = Request.Method.PUT
const val AND = "&"
const val SKIP = "skip="
const val COUNT = "count="
const val SLASH = "/"
const val QUERY = "?"
const val RESTAURANT = "restaurant"
const val MEAL = "meal"
const val CUISINES = "cuisines"
const val INGREDIENTS = "ingredients"
const val USER_QUERY = "user"

val RESTAURANTS_DTO = RestaurantsDto::class.java
val MEALS_DTO = MealsDto::class.java


class ApiRequester(val ctx: Context) {

    val dtoMapper = ObjectMapper()
        //Ignore unknown json fields
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        //Enable primary constructor on DTOs
        .registerModule(KotlinModule())

    /**
     * GETs
     */
    fun getRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        count: Int
    ): StringRequest {
        return httpServerRequest(
            GET,
            "",
            success,
            error,
            RESTAURANTS_DTO
        )
    }

    fun getMeals(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        count: Int
    ): StringRequest {
        return httpServerRequest(
            GET,
            "",
            success,
            error,
            MEALS_DTO
        )
    }

    /**
     * POSTs
     */

    /**
     * DELETEs
     */

    /**
     * PUTs
     */


    private fun <Model, Dto : IUnDto<Model>> httpServerRequest(
        method: Int,
        urlStr: String,
        onSuccess: (Model) -> Unit,
        onError: (VolleyError) -> Unit,
        dtoClass: Class<Dto>,
        payload: JSONObject? = null // No payload by default
    ): StringRequest {

        Log.v(TAG, urlStr)

        val responseToDtoTask: AsyncWorker<String, Model> =
            AsyncWorker<String, Model> {
                dtoMapper.readValue(it[0], dtoClass).unDto()
            }.setOnPostExecute(onSuccess)

        // Request a string response from the provided URL.
        return StringRequest(
            method,
            urlStr,
            Response.Listener(responseToDtoTask::execute),
            Response.ErrorListener { err -> onError(err) }
        )
    }
}
