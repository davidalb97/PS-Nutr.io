package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto
import pt.isel.ps.g06.httpserver.model.Moderator
import pt.isel.ps.g06.httpserver.model.User

@Component
class ModResponseMapper {

    fun mapToModel(dto: DbUserDto): Moderator =
            Moderator(
                    identifier = dto.submitterId,
                    userEmail = dto.email,
                    userPassword = dto.password
            )
}