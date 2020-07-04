package pt.isel.ps.g06.httpserver.security.dto

class UserRegisterRequest(
        val email: String,
        val username: String,
        val password: String
)