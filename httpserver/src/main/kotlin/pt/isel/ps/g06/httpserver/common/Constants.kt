package pt.isel.ps.g06.httpserver.common

const val PS_SERVER_SECRET = "PS_SERVER_SECRET" // Environment variable key
const val JWT_EXPIRATION = 1000 * 60 * 60 * 10 // 10 hours

// User roles
const val NORMAL_USER = "normal"
const val MOD_USER = "mod"

// Pagination
const val MAX_COUNT: Long = 30
const val DEFAULT_COUNT: Int = 15
const val DEFAULT_COUNT_STR = DEFAULT_COUNT.toString()