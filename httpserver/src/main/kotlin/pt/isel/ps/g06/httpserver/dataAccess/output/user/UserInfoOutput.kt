package pt.isel.ps.g06.httpserver.dataAccess.output.user

import pt.isel.ps.g06.httpserver.model.Submitter
import java.net.URI

class UserInfoOutput(
        val email: String,
        val username: String,
        val userRole: String,
        val userImage: URI?
)

fun mapUserToOutput(email: String, userRole: String, model: Submitter): UserInfoOutput =
        UserInfoOutput(
                email = email,
                username = model.name,
                userRole = userRole,
                userImage = model.image
        )