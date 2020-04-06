package pt.ipl.isel.leic.ps.androidclient.data.util

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class ServerRequest<T>(
    method: Int,
    url: String,
    private var klass: Class<T>,
    success: Response.Listener<T>,
    error: Response.ErrorListener
) : JsonRequest<T>(method, url, "", success, error) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<T> {
        //Log.v(TAG, "parseNetworkResponse on thread ${Thread.currentThread().name}")
        //Log.v(TAG, url)
        val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val gameDto = mapper.readValue(String(response.data), klass)
        //Log.v(TAG, "Success!!!!!!!!!! ${Thread.currentThread().name}")
        return Response.success(gameDto, null)
    }
}