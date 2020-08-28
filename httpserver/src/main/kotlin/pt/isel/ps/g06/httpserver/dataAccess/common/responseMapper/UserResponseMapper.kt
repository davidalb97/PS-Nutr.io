package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto
import pt.isel.ps.g06.httpserver.model.User

@Component
class UserResponseMapper {

    fun mapToModel(dto: DbUserDto): User =
            User(
                    identifier = dto.submitterId,
                    userEmail = dto.email,
                    userName = dto.username,
                    userPassword = dto.password,
                    userRole = dto.role,
                    isUserBanned = dto.isBanned
            )
}