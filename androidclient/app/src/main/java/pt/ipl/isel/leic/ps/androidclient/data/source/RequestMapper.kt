package pt.ipl.isel.leic.ps.androidclient.data.source

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
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

    // TODO: change
    fun buildUri(
        baseUri: String,
        parameters: HashMap<String, String>?
    ): String {

        if (parameters.isNullOrEmpty()) {
            return baseUri
        }

        var uri = baseUri

        parameters.forEach { parameter ->
            uri = uri.replace(parameter.key, parameter.value)
        }

        return uri
    }

    private var work: ((String?) -> Unit)? = null

    fun <Dto> asyncRequest(
        method: Method,
        urlStr: String,
        reqPayload: Any? = null
    ) {
        Log.v(TAG, urlStr)

        //Request payload serialization async worker
        AsyncWorker<Dto, Unit> {

            //Passed payload to String
            val payloadStr = jsonMapper.writeValueAsString(reqPayload)

            //Custom string request that will allow a string payload
            val jsonRequest =
                BodyStringRequest(
                    method.value,
                    urlStr,
                    payloadStr,
                    Response.Listener { work?.let { param -> param(it) } },
                    Response.ErrorListener { throw it }
                )
            requestQueue.add(jsonRequest)
        }.execute()
    }

    /**
     * Adds more work on top of the request
     */
    infix fun then(moreWork: (String?) -> Unit) {
        this.work = moreWork
    }

    /**
     * Maps to the respective data model.
     * The AsyncWorker's setOnPostExecute must be set
     * by who is calling this function after use it.
     * TODO - improve
     */
    fun <Dto, Model> thenMapTo(
        dtoClass: Class<Dto>,
        mappingFunc: (Dto) -> Model
    ): AsyncWorker<String?, Model> =
        AsyncWorker { mappingFunc(jsonMapper.readValue(it[0]!!, dtoClass)) }
}
