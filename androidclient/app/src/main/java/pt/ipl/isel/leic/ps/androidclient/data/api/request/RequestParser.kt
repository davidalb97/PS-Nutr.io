package pt.ipl.isel.leic.ps.androidclient.data.api.request

import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.fasterxml.jackson.databind.ObjectMapper
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger

private val log = Logger(RequestParser::class)

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
    fun <Dto> requestAndParse(
        method: HTTPMethod,
        uri: String,
        reqHeader: MutableMap<String, String>? = null,
        dtoClass: Class<Dto>,
        onSuccess: (Dto) -> Unit,
        onError: (VolleyError) -> Unit,
        reqPayload: Any? = null
    ) {
        request(method, uri, reqHeader, reqPayload, onError) { response ->
            deserializeBody(response.body, dtoClass) { deserializedBody ->
                onSuccess(deserializedBody)
            }
        }
    }


    /**
     * Makes an asynchronous request with an optional payload
     */
    fun request(
        method: HTTPMethod,
        uri: String,
        reqHeader: MutableMap<String, String>? = null,
        reqPayload: Any? = null,
        onError: (VolleyError) -> Unit,
        responseConsumer: (PayloadResponse) -> Unit
    ) {
        log.v(uri)

        //Serialize request payload to String
        val payloadStr = jsonMapper.writeValueAsString(reqPayload)

        //Custom string request that will allow a string payload
        val jsonRequest =
            JsonRequest(
                method = method,
                url = uri,
                reqHeader = reqHeader,
                reqPayload = payloadStr,
                listener = responseConsumer::invoke,
                onError = onError::invoke
            )
        requestQueue.add(jsonRequest)
    }

    /**
     * Parses string to Dto
     */
    private fun <Dto> deserializeBody(
        value: String,
        dtoClass: Class<Dto>,
        dtoConsumer: (Dto) -> Unit
    ) = dtoConsumer(jsonMapper.readValue(value, dtoClass))
}