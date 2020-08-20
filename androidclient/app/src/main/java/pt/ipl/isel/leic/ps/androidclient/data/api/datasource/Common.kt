package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

// Authorization header composition
const val AUTH_HEADER = "Authorization"
const val BEARER = "Bearer"

const val COUNT_PARAM = ":count"
const val SKIP_PARAM = ":skip"
const val SUBMITTER_QUERY = "?submitter"

// Returns the authorization header
fun buildAuthHeader(jwt: String): MutableMap<String, String> =
    mutableMapOf(Pair(AUTH_HEADER, "$BEARER $jwt"))

// Builds uris swapping the matching HashMap keys with the respective values
fun buildUri(
    baseUri: String,
    parameters: HashMap<String, String>?
): String {

    if (parameters.isNullOrEmpty()) {
        return baseUri
    }

    var uri = baseUri

    parameters.forEach { parameter ->
        uri = uri.replace(parameter.key, parameter.value)
    }

    return uri
}