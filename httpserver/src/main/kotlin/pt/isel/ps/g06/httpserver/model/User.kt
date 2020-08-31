package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.common.NORMAL_USER

class User(
        val identifier: Int,
        val userEmail: String,
        val userName: String,
        val userPassword: String,
        val userRole: String = NORMAL_USER,
        val isUserBanned: Boolean
)