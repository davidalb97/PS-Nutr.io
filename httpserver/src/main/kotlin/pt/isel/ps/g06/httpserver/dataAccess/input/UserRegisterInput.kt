package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

class UserRegisterInput(
        //@field:NotNull(message = "An email must be given!")
        @field:Pattern(
                //regexp = "/^([a-z0-9_\\.\\+-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})\$/",
                regexp = "^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$",
                message = "Given email must follow a valid email pattern!"
        )
        val email: String,

        @field:NotBlank(message = "An username must be given!")
        val username: String,

        @field:NotBlank(message = "A password must be given!")
        val password: String
)