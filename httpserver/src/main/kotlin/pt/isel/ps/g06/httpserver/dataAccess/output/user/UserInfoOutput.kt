package pt.isel.ps.g06.httpserver.dataAccess.output.user

import pt.isel.ps.g06.httpserver.model.User

class UserInfoOutput(
        val email: String,
        val username: String,
        val userRole: String
)

fun mapUserToOutput(user: User): UserInfoOutput =
        UserInfoOutput(
                email = user.userEmail,
                username = user.userName,
                userRole = user.userRole
        )