package pt.ipl.isel.leic.ps.androidclient.data.api.request

import com.android.volley.Request

/**
 * HTTP methods enum
 */
enum class HTTPMethod(val value: Int) {
    GET(Request.Method.GET),
    POST(Request.Method.POST),
    PUT(Request.Method.PUT),
    DELETE(Request.Method.DELETE)
}