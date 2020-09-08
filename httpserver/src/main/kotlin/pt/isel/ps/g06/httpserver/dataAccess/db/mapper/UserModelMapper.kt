package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto
import pt.isel.ps.g06.httpserver.model.User

@Component
class UserModelMapper : ModelMapper<DbUserDto, User> {

    override fun mapTo(dto: DbUserDto): User {
        return User(
                identifier = dto.submitterId,
                userEmail = dto.email,
                userName = dto.username,
                userPassword = dto.password,
                userRole = dto.role,
                isUserBanned = dto.isBanned
        )
    }
}