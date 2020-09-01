package pt.ipl.isel.leic.ps.androidclient.data.api.datasource

// Authorization header composition
const val AUTH_HEADER = "Authorization"
const val BEARER = "Bearer"

const val COUNT_PARAM = "count"
const val SKIP_PARAM = "skip"
const val CUISINES_PARAM = "cuisines"
const val CUSTOM_PATH = "custom"
const val SUGGESTED_PATH = "suggested"

const val FAVORITE_PATH = "favorite"
const val VOTE_PATH = "vote"
const val REPORT_PATH = "report"

const val ADDRESS = "10.0.2.2" // Loopback for the host machine
const val PORT = "8080"
const val SCHEME = "http"
const val ADDRESS_PORT = "$ADDRESS:$PORT"

const val MEAL_PATH = "meal"
const val RESTAURANT_PATH = "restaurant"
const val CUISINES_PATH = "cuisine"
const val INGREDIENTS_PATH = "ingredient"
const val INSULIN_PROFILE_PATH = "profile"

// Returns the authorization header
fun buildAuthHeader(jwt: String): MutableMap<String, String> =
    mutableMapOf(Pair(AUTH_HEADER, "$BEARER $jwt"))