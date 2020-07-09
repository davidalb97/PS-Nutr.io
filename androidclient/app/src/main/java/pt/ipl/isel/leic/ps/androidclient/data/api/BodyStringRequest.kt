package pt.ipl.isel.leic.ps.androidclient.data.api

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import java.io.UnsupportedEncodingException

class BodyStringRequest : StringRequest {

    private val PROTOCOL_CHARSET = "utf-8"
    private val PROTOCOL_CONTENT_TYPE =
        String.format("application/json; charset=%s", PROTOCOL_CHARSET)
    private val reqHeader: MutableMap<String, String>?
    private val reqPayload: String?

    constructor(
        method: Int,
        url: String?,
        reqHeader: MutableMap<String, String>?,
        reqPayload: String?,
        listener: Response.Listener<String>?,
        errorListener: Response.ErrorListener?
    ) : super(method, url, listener, errorListener) {
        this.reqHeader = reqHeader
        this.reqPayload = reqPayload
    }


    constructor(
        url: String?,
        reqHeader: MutableMap<String, String>?,
        reqPayload: String?,
        listener: Response.Listener<String?>?,
        errorListener: Response.ErrorListener?
    ) : super(url, listener, errorListener) {
        this.reqHeader = reqHeader
        this.reqPayload = reqPayload
    }

    override fun getHeaders(): MutableMap<String, String> = this.reqHeader ?: mutableMapOf()

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
    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
        var parsed = ""

        val encoding = charset(PROTOCOL_CHARSET)

        try {
            parsed = String(response.data, encoding)
            val bytes = parsed.toByteArray(encoding)
            parsed = String(bytes, charset(PROTOCOL_CHARSET))

            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            return Response.error(ParseError(e))
        }
    }

    override fun getBodyContentType() = PROTOCOL_CONTENT_TYPE
}