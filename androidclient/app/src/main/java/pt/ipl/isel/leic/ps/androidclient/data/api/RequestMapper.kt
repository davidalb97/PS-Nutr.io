package pt.ipl.isel.leic.ps.androidclient.data.api

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.fasterxml.jackson.databind.ObjectMapper
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

const val ADDRESS = "10.0.2.2" // Loopback for the host machine
const val PORT = "8080"
const val URI_BASE = "http://$ADDRESS:$PORT"
const val SKIP = "&skip="
const val COUNT = "&count="

const val MEAL = "meal"
const val CUISINES = "cuisines"
const val URI_END = "$SKIP=:skip$COUNT=:count"
const val INGREDIENTS = "ingredients"
const val USER_QUERY = "user"

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
 * This class makes asynchronous HTTP requests and maps
 * the results to their respective data models
 */
class RequestMapper(
    private val requestQueue: RequestQueue,
    private val jsonMapper: ObjectMapper
) {

    /**
     * A all-in-one function: requests, parses string to Dto and
     * maps the Dtos to the respective data model using the functions
     * below.
     */
    fun <Dto, Model> requestAndRespond(
        method: Method,
        urlStr: String,
        dtoClass: Class<Dto>,
        mappingFunc: (Dto) -> Model,
        onSuccess: (Model) -> Unit,
        onError: (VolleyError) -> Unit,
        reqPayload: Any? = null
    ) {
        request(method, urlStr, reqPayload, onError) { strResponse ->
            parseDto(strResponse, dtoClass) { dtoArray ->
                onSuccess(mappingFunc(dtoArray))
            }
        }
    }

    /**
     * Makes an asynchronous request with an optional payload
     */
    fun request(
        method: Method,
        urlStr: String,
        reqPayload: Any? = null,
        onError: (VolleyError) -> Unit,
        responseConsumer: (String) -> Unit
    ) {
        Log.v(TAG, urlStr)

        //Request payload serialization async worker
        AsyncWorker<Unit, Unit> {

            //Passed payload to String
            val payloadStr = jsonMapper.writeValueAsString(reqPayload)

            //Custom string request that will allow a string payload
            val jsonRequest =
                BodyStringRequest(
                    method.value,
                    urlStr,
                    payloadStr,
                    Response.Listener { responseConsumer(it!!) },
                    Response.ErrorListener { onError(it) }
                )
            requestQueue.add(jsonRequest)
        }.execute()
    }

    /**
     * Parses string to Dto
     */
    fun <Dto> parseDto(
        value: String,
        dtoClass: Class<Dto>,
        dtoConsumer: (Dto) -> Unit
    ) = dtoConsumer(jsonMapper.readValue(value, dtoClass))
}