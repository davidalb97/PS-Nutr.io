package pt.ipl.isel.leic.ps.androidclient.data.source

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.CuisinesDto
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.IUnDto
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.MealsDto
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.RestaurantsDto
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

const val ADDRESS = "localhost"
const val PORT = "8080"
const val URI_BASE = "http://$ADDRESS:$PORT"
const val AND = "&"
const val SKIP = "skip="
const val COUNT = "count="
const val RESTAURANT = "restaurant"
const val MEAL = "meal"
const val CUISINES = "cuisines"
const val INGREDIENTS = "ingredients"
const val USER_QUERY = "user"

val RESTAURANTS_DTO = RestaurantsDto::class.java
val MEALS_DTO = MealsDto::class.java
val CUISINES_DTO = CuisinesDto::class.java

/**
 * HTTP methods enum
 */
enum class Method(val value: Int) {
    GET(Request.Method.GET),
    POST(Request.Method.POST),
    PUT(Request.Method.PUT),
    DELETE(Request.Method.DELETE)
}

/**
 * This class contains the endpoints available for this app
 */
class DataSource(ctx: Context) {

    val dtoMapper = ObjectMapper()
        //Ignore unknown json fields
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        //Enable primary constructor on DTOs
        .registerModule(KotlinModule())

    val queue = Volley.newRequestQueue(ctx)

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        count: Int,
        skip: Int
    ) {
        httpServerRequest(
            Method.GET,
            "$URI_BASE/$RESTAURANT",
            RESTAURANTS_DTO,
            success,
            error,
            null
        )
    }

    fun getMeals(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        count: Int,
        skip: Int
    ) {
        httpServerRequest(
            Method.GET,
            "$URI_BASE/$MEAL",
            MEALS_DTO,
            success,
            error,
            null
        )
    }

    fun getCuisines(
        success: (List<Cuisine>) -> Unit,
        error: (VolleyError) -> Unit,
        count: Int,
        skip: Int
    ) {
        httpServerRequest(
            Method.GET,
            "$URI_BASE/$CUISINES",
            CUISINES_DTO,
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

    /**
     * A generic Volley's requester
     */
    private fun <Model, Dto : IUnDto<Model>, ReqPayload> httpServerRequest(
        method: Method,
        urlStr: String,
        dtoClass: Class<Dto>,
        onSuccess: (Model) -> Unit,
        onError: (VolleyError) -> Unit = { Log.v(TAG, it.toString()) },
        reqPayload: ReqPayload?
    ) {

        Log.v(TAG, urlStr)

        //Response payload serialization async worker
        val responseToDtoTask: AsyncWorker<String?, Model> =
            AsyncWorker<String?, Model> {
                dtoMapper.readValue(it[0], dtoClass).unDto()
            }.setOnPostExecute(onSuccess)

        //Request payload serialization async worker
        AsyncWorker<Model, Unit> {

            //Create a string payload from
            val payloadStr = dtoMapper.writeValueAsString(reqPayload)

            //Custom string request that will allow a string payload
            val jsonRequest = BodyStringRequest(
                method.value,
                urlStr,
                payloadStr,
                Response.Listener {
                    responseToDtoTask.execute(it)
                },
                Response.ErrorListener {
                    onError(it)
                }
            )
            queue.add(jsonRequest)
        }.execute()
    }
}
