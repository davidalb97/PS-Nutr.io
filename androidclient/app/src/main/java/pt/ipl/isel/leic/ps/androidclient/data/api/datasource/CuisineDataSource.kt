package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.*
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.CuisinesInput

private const val COUNT_PARAM = ":count"
private const val SKIP_PARAM = ":skip"
private const val CUISINE_URI = "$URI_BASE/$CUISINES" +
    "?skip=$SKIP_PARAM" +
    "&count=$COUNT_PARAM"

class CuisineDataSource(
    private val requestParser: RequestParser,
    private val uriBuilder: UriBuilder
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
        uri = uriBuilder.buildUri(uri, params)
        requestParser.requestAndRespond(
            method = Method.GET,
            urlStr = uri,
            dtoClass = CuisinesInput::class.java,
            onSuccess = success,
            onError = error
        )
    }
}