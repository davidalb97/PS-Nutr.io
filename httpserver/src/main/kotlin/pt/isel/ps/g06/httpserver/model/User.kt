package pt.isel.ps.g06.httpserver.model

class User(
        val identifier: Int,
        val userEmail: String,
        val userPassword: String,
        val userRole: String,
        val isUserBanned: Boolean
)