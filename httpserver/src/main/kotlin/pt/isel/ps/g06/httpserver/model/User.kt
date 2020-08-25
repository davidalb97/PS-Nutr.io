package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.common.NORMAL_USER

open class User(
        val identifier: Int,
        val userEmail: String,
        val userPassword: String,
        val userRole: String = NORMAL_USER,
        val isUserBanned: Boolean
)