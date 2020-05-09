package pt.ipl.isel.leic.ps.androidclient.data.source

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.*
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

const val ADDRESS = "10.0.2.2" // Loopback for the host machine
const val PORT = "8080"
const val URI_BASE = "http://$ADDRESS:$PORT"
const val SKIP = "&skip="
const val COUNT = "&count="
const val RESTAURANT = "restaurant"
const val MEAL = "meal"
const val CUISINES = "cuisines"
const val URI_END = "$SKIP=:skip$COUNT=:count"
const val INGREDIENTS = "ingredients"
const val USER_QUERY = "user"

const val RESTAURANT_ID_URI =
    "$URI_BASE/$RESTAURANT/:id"
const val MEAL_ID_URI =
    "$URI_BASE/$MEAL/:id"
const val CUISINE_ID_URI =
    "$URI_BASE/$CUISINES/:name"

val RESTAURANTS_DTO = RestaurantsDto::class.java
val RESTAURANT_DTO = RestaurantDto::class.java
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

    // Jackson deserializer
    val dtoMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    val queue = Volley.newRequestQueue(ctx)

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getRestaurantById(
        success: (Restaurant) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = RESTAURANT_ID_URI

        uri =
            if (uriParameters!!["path"]?.isEmpty()!!)
                uri.removeSuffix("/:id/")
            else
                buildUri(uri, uriParameters)


        httpServerRequest(
            Method.GET,
            uri,
            RESTAURANT_DTO,
            success,
            error,
            null
        )
    }

    // TODO
    fun getNearbyRestaurants(
        success: (List<Restaurant>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = RESTAURANT_ID_URI

        uri =
            if (uriParameters.isNullOrEmpty())
                uri.removeSuffix("/:id")
            else
                buildUri(uri, uriParameters)


        httpServerRequest(
            Method.GET,
            uri,
            RESTAURANTS_DTO,
            success,
            error,
            null
        )
    }

    fun getMeals(
        success: (List<Meal>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = MEAL_ID_URI

        uri =
            if (uriParameters.isNullOrEmpty())
                uri.removeSuffix("/:id")
            else
                buildUri(uri, uriParameters)

        httpServerRequest(
            Method.GET,
            uri,
            MEALS_DTO,
            success,
            error,
            null
        )
    }

    fun getCuisines(
        success: (List<Cuisine>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri = CUISINE_ID_URI

        uri =
            if (uriParameters.isNullOrEmpty())
                uri.removeSuffix("/:name")
            else
                buildUri(uri, uriParameters)
        httpServerRequest(
            Method.GET,
            uri,
            CuisinesDto::class.java,
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
     * Uri builder
     */
    private fun buildUri(
        baseUri: String,
        parameters: HashMap<String, HashMap<String, String>>
    ): String {

        val path = parameters["path"]
        val query = parameters["query"]

        var uri = baseUri

        path?.forEach { parameter ->
            uri = uri.replace(parameter.key, parameter.value)
        }

        query?.forEach { parameter ->
            uri = uri.replace(parameter.key, parameter.value)
        }

        return uri
    }

    /**
     * A generic Volley's requester
     */
    private fun <Model, Dto : IUnDto<Model>, ReqPayload> httpServerRequest(
        method: Method,
        urlStr: String,
        dtoClass: Class<Dto>,
        onSuccess: (Model) -> Unit,
        onError: (VolleyError) -> Unit,
        reqPayload: ReqPayload?
    ) {

        Log.v(TAG, urlStr)

        //Response payload deserialization async worker
        val responseToDtoTask: AsyncWorker<String?, Model> =
            AsyncWorker<String?, Model> {
                dtoMapper.readValue(it[0]!!, dtoClass).unDto()
            }.setOnPostExecute(onSuccess)

        //Request payload serialization async worker
        AsyncWorker<Model, Unit> {

            //Passed payload to String
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
