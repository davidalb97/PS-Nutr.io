package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.CUISINES
import pt.ipl.isel.leic.ps.androidclient.data.api.Method
import pt.ipl.isel.leic.ps.androidclient.data.api.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.URI_BASE
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.CuisinesInput

private const val CUISINE_URI = "$URI_BASE/$CUISINES" +
        "?skip=$SKIP_PARAM" +
        "&count=$COUNT_PARAM"

class CuisineDataSource(
    private val requestParser: RequestParser
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getCuisines(
        count: Int,
        skip: Int,
        success: (CuisinesInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        var uri = CUISINE_URI
        val params = hashMapOf(
            Pair(SKIP_PARAM, "$skip"),
            Pair(COUNT_PARAM, "$count")
        )

        uri = buildUri(uri, params)

        requestParser.requestAndRespond(
            method = Method.GET,
            uri = uri,
            dtoClass = CuisinesInput::class.java,
            onSuccess = success,
            onError = error
        )
    }
}