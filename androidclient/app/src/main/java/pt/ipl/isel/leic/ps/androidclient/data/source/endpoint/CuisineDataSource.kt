package pt.ipl.isel.leic.ps.androidclient.data.source.endpoint

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.source.*
import pt.ipl.isel.leic.ps.androidclient.data.source.dtos.CuisinesDto
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine

private const val CUISINE_ID_URI =
    "$URI_BASE/$CUISINES/:name"

private val CUISINES_DTO = CuisinesDto::class.java

class CuisineDataSource(
    private val requester: Requester
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getCuisines(
        success: (List<Cuisine>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            CUISINE_ID_URI

        uri =
            if (uriParameters.isNullOrEmpty())
                uri.removeSuffix("/:name")
            else
                requester.buildUri(uri, uriParameters)
        requester.httpServerRequest(
            Method.GET,
            uri,
            CuisinesDto::class.java,
            success,
            error,
            null
        )
    }

    /**
     * ----------------------------- POSTs -----------------------------
     */

    /**
     * ----------------------------- DELETEs ---------------------------
     */

    /**
     * ----------------------------- PUTs ------------------------------
     */
}