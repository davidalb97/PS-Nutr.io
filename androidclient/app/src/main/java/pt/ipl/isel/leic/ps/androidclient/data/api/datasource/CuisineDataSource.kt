package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import pt.ipl.isel.leic.ps.androidclient.data.source.*
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine

private const val CUISINE_ID_URI =
    "$URI_BASE/$CUISINES/:name"

//private val CUISINES_DTO = CuisinesIDto::class.java

class CuisineDataSource(
    private val requestMapper: RequestMapper
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    /*fun getCuisines(
        success: (List<Cuisine>) -> Unit,
        error: (VolleyError) -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>?,
        count: Int,
        skip: Int
    ) {
        var uri =
            CUISINE_ID_URI

        uri = requester.buildUri(uri, uriParameters)
        requester.httpServerRequest(
            Method.GET,
            uri,
            CuisinesIDto::class.java,
            success,
            error,
            null
        )
    }*/

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