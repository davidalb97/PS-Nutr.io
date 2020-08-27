package pt.isel.ps.g06.httpserver.common

const val PS_SERVER_SECRET = "PS_SERVER_SECRET" // Environment variable key
const val JWT_EXPIRATION = 1000 * 60 * 60 * 10 // 10 hours

// User roles
const val NORMAL_USER = "normal"
const val MOD_USER = "mod"

// Pagination
const val COUNT: Long = 15