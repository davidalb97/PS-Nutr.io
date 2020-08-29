package pt.isel.ps.g06.httpserver.dataAccess.input.user

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

class UserRegisterInput(
        @field:NotNull(message = "An email must be given!")
        @field:Pattern(
                regexp = "^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$",
                message = "Given email must follow a valid email pattern!"
        )
        val email: String,

        @field:NotNull(message = "An username must be given!")
        val username: String,

        @field:NotNull(message = "A password must be given!")
        val password: String
)