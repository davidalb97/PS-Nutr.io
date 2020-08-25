package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.CuisinesInput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.*
import pt.ipl.isel.leic.ps.androidclient.data.util.appendQueryNotNullParameter

class CuisineDataSource(
    private val requestParser: RequestParser
) {

    /**
     * ----------------------------- GETs -----------------------------
     */
    fun getCuisines(
        count: Int?,
        skip: Int?,
        success: (CuisinesInput) -> Unit,
        error: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .encodedAuthority(ADDRESS_PORT)
                .appendPath(CUISINES_PATH)
                .appendQueryNotNullParameter(COUNT_PARAM, count)
                .appendQueryNotNullParameter(SKIP_PARAM, skip)
                .build()
                .toString(),
            dtoClass = CuisinesInput::class.java,
            onSuccess = success,
            onError = error
        )
    }
}