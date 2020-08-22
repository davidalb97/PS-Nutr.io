package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import android.net.Uri
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.InsulinProfileInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.InsulinProfileOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.*

class InsulinProfileDataSource(
    private val requestParser: RequestParser
) {

    fun getAllInsulinProfiles(
        jwt: String,
        onSuccess: (Array<InsulinProfileInput>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(USER)
                .appendPath(INSULIN_PROFILE_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            dtoClass = Array<InsulinProfileInput>::class.java,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun getInsulinProfile(
        jwt: String,
        name: String,
        onSuccess: (InsulinProfileInput) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        requestParser.requestAndParse(
            method = HTTPMethod.GET,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(USER)
                .appendPath(INSULIN_PROFILE_PATH)
                .appendPath(name)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            dtoClass = InsulinProfileInput::class.java,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun postInsulinProfile(
        insulinProfileOutput: InsulinProfileOutput,
        jwt: String,
        onError: (VolleyError) -> Unit
    ) {
        requestParser.request(
            method = HTTPMethod.POST,
            uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(USER)
                .appendPath(INSULIN_PROFILE_PATH)
                .build()
                .toString(),
            reqHeader = buildAuthHeader(jwt),
            reqPayload = insulinProfileOutput,
            onError = onError,
            responseConsumer = {}
        )
    }

    fun deleteInsulinProfile(
        profileName: String,
        jwt: String,
        onError: (VolleyError) -> Unit
    ) {
        requestParser.request(
            HTTPMethod.DELETE,
            Uri.Builder()
                .scheme(SCHEME)
                .authority(ADDRESS_PORT)
                .appendPath(USER)
                .appendPath(INSULIN_PROFILE_PATH)
                .appendPath(profileName)
                .build()
                .toString(),
            buildAuthHeader(jwt),
            null,
            onError = onError,
            responseConsumer = {}
        )
    }
}