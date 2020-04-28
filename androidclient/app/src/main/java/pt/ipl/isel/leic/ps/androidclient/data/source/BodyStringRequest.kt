package pt.ipl.isel.leic.ps.androidclient.data.source

import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import java.io.UnsupportedEncodingException

class BodyStringRequest : StringRequest {

    private val PROTOCOL_CHARSET = "utf-8"
    private val PROTOCOL_CONTENT_TYPE =
        String.format("application/json; charset=%s", PROTOCOL_CHARSET)
    private val reqPayload: String?

    constructor(
        method: Int,
        url: String?,
        reqPayload: String?,
        listener: Response.Listener<String?>?,
        errorListener: Response.ErrorListener?
    ) : super(method, url, listener, errorListener) {
        this.reqPayload = reqPayload
    }


    constructor(
        url: String?,
        reqPayload: String?,
        listener: Response.Listener<String?>?,
        errorListener: Response.ErrorListener?
    ) : super(url, listener, errorListener) {
        this.reqPayload = reqPayload
    }


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

    override fun getBodyContentType() = PROTOCOL_CONTENT_TYPE
}