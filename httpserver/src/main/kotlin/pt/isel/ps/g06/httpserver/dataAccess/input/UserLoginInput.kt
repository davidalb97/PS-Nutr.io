package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.constraints.NotBlank

class UserLoginInput(
        @field:NotBlank(message = "An email must be given!")
        val email: String,
        @field:NotBlank(message = "A password must be given!")
        val password: String
)