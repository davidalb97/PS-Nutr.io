package pt.isel.ps.g06.httpserver.dataAccess.output.security

import java.net.URI

class UserInfoOutput(
        val email: String,
        val username: String,
        val userImage: URI?
)