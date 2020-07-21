package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.INSULIN_PROFILE
import pt.ipl.isel.leic.ps.androidclient.data.api.Method
import pt.ipl.isel.leic.ps.androidclient.data.api.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.URI_BASE
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.InsulinProfileInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.InsulinProfileOutput

private const val INSULIN_PROFILE_PARAM = ":profileName"

private const val INSULIN_PROFILES_URI = "$URI_BASE/$USER/$INSULIN_PROFILE"
private const val INSULIN_PROFILE_URI = "$INSULIN_PROFILES_URI/$INSULIN_PROFILE_PARAM"

class InsulinProfileDataSource(
    private val requestParser: RequestParser
) {

    fun getAllInsulinProfiles(
        jwt: String,
        onSuccess: (Array<InsulinProfileInput>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        requestParser.requestAndRespond(
            Method.GET,
            INSULIN_PROFILES_URI,
            buildAuthHeader(jwt),
            Array<InsulinProfileInput>::class.java,
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

        val uri = buildUri(INSULIN_PROFILES_URI, hashMapOf(Pair(INSULIN_PROFILE_PARAM, name)))

        requestParser.requestAndRespond(
            Method.GET,
            uri,
            buildAuthHeader(jwt),
            InsulinProfileInput::class.java,
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
            Method.POST,
            INSULIN_PROFILES_URI,
            buildAuthHeader(jwt),
            insulinProfileOutput,
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
            Method.DELETE,
            INSULIN_PROFILES_URI,
            buildAuthHeader(jwt),
            profileName,
            onError = onError,
            responseConsumer = {}
        )
    }
}