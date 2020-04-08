package pt.ipl.isel.leic.ps.androidclient.data.sources

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.data.sources.dtos.*
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


class ApiRequester(val ctx: Context) {

    val dtoMapper = ObjectMapper()
        //Ignore unknown json fields
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        //Enable primary constructor on DTOs
        .registerModule(KotlinModule())
    private val queue = Volley.newRequestQueue(ctx)

    /**
     * GETs
     */
    fun getRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        count: Int
    ) {
        httpServerGetRequest(
            "",
            success,
            error,
            RestaurantsDto::class.java
        )
    }

    fun getMeals(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        count: Int
    ) {
        httpServerGetRequest(
            "",
            success,
            error,
            MealsDto::class.java
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

    private fun <Model, Dto: IUnDto<Model>> httpServerGetRequest(
        urlStr: String,
        onSuccess: (Model) -> Unit,
        onError: (VolleyError) -> Unit,
        dtoClass: Class<Dto>
    ) {

        Log.v(TAG, urlStr)

        val responseToDtoTask: AsyncWorker<String, Model> =
            AsyncWorker<String, Model> {
                dtoMapper.readValue(it[0], dtoClass).unDto()
            }.setOnPostExecute(onSuccess)

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            GET,
            urlStr,
            Response.Listener(responseToDtoTask::execute),
            Response.ErrorListener { err -> onError(err) }
        )
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    /*
    private fun <P, R> httpServerMethodRequest(
        urlStr: String,
        onSuccess: (R) -> Unit,
        onError: (VolleyError) -> Unit,
        dtoClass: Class<R>,
        intMethod: Int, //Ex: Request.Method.GET
        payload: JSONObject?
    ) : AsyncWorker<String, R> {

        Log.v(TAG, urlStr)

        val responseToDtoTask: AsyncWorker<JSONObject, R> =
            AsyncWorker<JSONObject, R> {
                it.toString()
                dtoMapper.readValue()
                it[0].
                .readValue(it[0], dtoClass)
            }.setOnPostExecute(onSuccess)

        val jsonRequest = JsonObjectRequest(
            intMethod,
            urlStr,
            payload,
            Response.Listener(responseToDtoTask::execute),
            Response.ErrorListener { err -> onError(err) }
        )
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            intMethod,
            urlStr,
            Response.Listener(responseToDtoTask::execute),
            Response.ErrorListener { err -> onError(err) }
        )
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
        return responseToDtoTask
    }*/
}
