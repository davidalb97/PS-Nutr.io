package pt.ipl.isel.leic.ps.androidclient.data.source

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.fasterxml.jackson.databind.ObjectMapper
import pt.ipl.isel.leic.ps.androidclient.TAG
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.IResponseMapper
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
 * This class contains the endpoints available for this app
 */
class Requester(
    private val requestQueue: RequestQueue,
    private val jsonMapper: ObjectMapper
) {
    /**
     * Uri builder
     */
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

    /**
     * A generic Volley's requester - TODO
     */
    fun <Model, Dto> httpServerRequest(
        method: Method,
        urlStr: String,
        dtoClass: Class<Dto>,
        mappingFunction: (Dto) -> Model,
        onSuccess: (Model) -> Unit,
        onError: (VolleyError) -> Unit,
        reqPayload: Any? = null
    ) {

        Log.v(TAG, urlStr)

        //Response payload deserialization async worker
        val dtoToModelResponseTask: AsyncWorker<String?, Model> =
            AsyncWorker<String?, Model> {
                mappingFunction(jsonMapper.readValue(it[0], dtoClass))
            }.setOnPostExecute(onSuccess)

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
                    Response.Listener {
                        dtoToModelResponseTask.execute(it)
                    },
                    Response.ErrorListener {
                        onError(it)
                    }
                )
            requestQueue.add(jsonRequest)
        }.execute()
    }
}
