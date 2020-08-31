package pt.ipl.isel.leic.ps.androidclient.data.api.request

import androidx.annotation.GuardedBy
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import java.io.UnsupportedEncodingException

private const val PROTOCOL_CHARSET = "utf-8"
private const val PROTOCOL_CONTENT_TYPE = "application/json; charset=$PROTOCOL_CHARSET"

private val logger = Logger(JsonRequest::class)

class JsonRequest(
    val method: HTTPMethod,
    url: String?,
    private val reqHeader: Map<String, String>?,
    private val reqPayload: String?,
    @GuardedBy("mLock")
    private var listener: Response.Listener<PayloadResponse>?,
    private val onError: Response.ErrorListener?
) : Request<PayloadResponse>(method.value, url, { error ->

    onError?.onErrorResponse(error)

    logger.v("\nReceived response from $method $url, " +
            "\nstatus: ${error?.networkResponse?.statusCode} " +
            "\nbody: ${error?.networkResponse?.data?.let(::String)} ")

}) {



    /** Lock to guard mListener as it is cleared on cancel() and read on delivery.  */
    private val mLock = Any()

    override fun getHeaders(): Map<String, String> = this.reqHeader ?: emptyMap()

    override fun getBody(): ByteArray? {
        return try {
            return reqPayload?.toByteArray(charset(PROTOCOL_CHARSET)) ?: super.getBody()
        } catch (uee: UnsupportedEncodingException) {
            VolleyLog.wtf(
                "Unsupported Encoding while trying to get the bytes of %s using %s",
                reqPayload,
                PROTOCOL_CHARSET
            )
            null
        }
    }

    /**
     * Overriding to set the charset to UTF-8
     */
    override fun parseNetworkResponse(response: NetworkResponse): Response<PayloadResponse> {
        val encoding = charset(PROTOCOL_CHARSET)
        var logMsg = "Received response from $method $url, status: ${response.statusCode}"
        return try {
            val body = String(response.data, encoding)
            when {
                response.statusCode >= 500 -> {
                    logMsg += ", error body: $body"
                    Response.error(ServerError(response))
                }
                response.statusCode >= 400 -> {
                    logMsg += ", error body: $body"
                    Response.error(ClientError(response))
                }
                else -> {
                    Response.success(
                        PayloadResponse(
                            headers = response.headers,
                            status = response.statusCode,
                            body = body
                        ),
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } finally {
            logger.v(logMsg)
        }
    }

    override fun deliverResponse(response: PayloadResponse?) {
        var listener: Response.Listener<PayloadResponse>?
        synchronized(mLock) {
            listener = this.listener
        }
        if (listener != null) {
            listener!!.onResponse(response)
        }
    }

    override fun cancel() {
        super.cancel()
        synchronized(mLock) {
            listener = null
        }
    }

    override fun getBodyContentType() = PROTOCOL_CONTENT_TYPE
}