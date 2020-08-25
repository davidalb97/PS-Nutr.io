package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.common.MOD_USER

class Moderator (
        identifier: Int,
        userEmail: String,
        userPassword: String
): User(
        identifier = identifier,
        userEmail = userEmail,
        userPassword = userPassword,
        userRole = MOD_USER,
        isUserBanned = false
)