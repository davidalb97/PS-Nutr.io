package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class DbUserDto(
        val submitterId: Int,
        val email: String,
        val password: String,
        val role: String,
        val isBanned: Boolean
)