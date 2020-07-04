package pt.isel.ps.g06.httpserver.security

class RegisterInputDto(
        val submitterId: Int,
        val email: String,
        val password: String
)