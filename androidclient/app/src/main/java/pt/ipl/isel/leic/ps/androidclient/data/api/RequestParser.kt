package pt.ipl.isel.leic.ps.androidclient.data.api

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.fasterxml.jackson.databind.ObjectMapper
import pt.ipl.isel.leic.ps.androidclient.TAG

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
 * This class makes asynchronous HTTP requests and parses
 * the received data to objects (DTOs), as specified by
 * the repository pattern.
 * @Link https://miro.medium.com/max/1130/1*xxr1Idc8UoNELOzqXcJnag.png
 */
class RequestParser(
    private val requestQueue: RequestQueue,
    private val jsonMapper: ObjectMapper
) {

    /**
     * A all-in-one function: requests and parses the string response to Dto
     */
    fun <Dto> requestAndRespond(
        method: Method,
        urlStr: String,
        dtoClass: Class<Dto>,
        onSuccess: (Dto) -> Unit,
        onError: (VolleyError) -> Unit,
        reqPayload: Any? = null
    ) {
        request(method, urlStr, reqPayload, onError) { strResponse ->
            parseDto(strResponse, dtoClass) { dtoArray ->
                onSuccess(dtoArray)
            }
        }
    }

    /*suspend fun <Dto> coroutineRequestAndParse(
        method: Method,
        urlStr: String,
        dtoClass: Class<Dto>,
        onError: (Exception) -> Unit,
        reqPayload: Any? = null
    ): Dto? {
        return withContext(Dispatchers.IO) {
            try {
                val rsp = coroutineRequest(method, urlStr, reqPayload)
                return@withContext jsonMapper.readValue(rsp, dtoClass)
            } catch (e: Exception) {
                onError(e)
                return@withContext null
            }
        }
    }*/

    /*suspend fun coroutineRequest(
        method: Method,
        urlStr: String,
        reqPayload: Any? = null
    ) = suspendCoroutine<String> { continuation ->
        //Request payload serialization async worker
        //Passed payload to String
        val payloadStr = jsonMapper.writeValueAsString(reqPayload)

        //Custom string request that will allow a string payload
        val jsonRequest =
            BodyStringRequest(
                method.value,
                urlStr,
                payloadStr,
                Response.Listener { continuation.resume(it) },
                Response.ErrorListener { continuation.resumeWithException(it) }
            )
        requestQueue.add(jsonRequest)
    }*/

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

        //Serialize request payload to String
        val payloadStr = jsonMapper.writeValueAsString(reqPayload)

        //Custom string request that will allow a string payload
        val jsonRequest =
            BodyStringRequest(
                method.value,
                urlStr,
                payloadStr,
                Response.Listener { stringResponse -> responseConsumer(stringResponse) },
                Response.ErrorListener { error -> onError(error) }
            )
        requestQueue.add(jsonRequest)
    }

    /**
     * Parses string to Dto
     */
    private fun <Dto> parseDto(
        value: String,
        dtoClass: Class<Dto>,
        dtoConsumer: (Dto) -> Unit
    ) = dtoConsumer(jsonMapper.readValue(value, dtoClass))
}