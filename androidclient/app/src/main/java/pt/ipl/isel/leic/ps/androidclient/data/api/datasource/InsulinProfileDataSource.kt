package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.InsulinProfileInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.InsulinProfileOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.request.HTTPMethod
import pt.ipl.isel.leic.ps.androidclient.data.api.request.INSULIN_PROFILE
import pt.ipl.isel.leic.ps.androidclient.data.api.request.RequestParser
import pt.ipl.isel.leic.ps.androidclient.data.api.request.URI_BASE

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
        requestParser.requestAndParse(
            HTTPMethod.GET,
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

        val uri = buildUri(INSULIN_PROFILE_URI, hashMapOf(Pair(INSULIN_PROFILE_PARAM, name)))

        requestParser.requestAndParse(
            HTTPMethod.GET,
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
            HTTPMethod.POST,
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
            HTTPMethod.DELETE,
            INSULIN_PROFILES_URI,
            buildAuthHeader(jwt),
            profileName,
            onError = onError,
            responseConsumer = {}
        )
    }
}