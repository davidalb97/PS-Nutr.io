package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.UserDbRepository
import pt.isel.ps.g06.httpserver.model.Creator

@Component
class DbCreatorResponseMapper(
        private val dbUserRepo: UserDbRepository
) : ResponseMapper<DbSubmitterDto, Creator> {
    override fun mapTo(dto: DbSubmitterDto): Creator {
        val userDto = dbUserRepo.getBySubmitterId(dto.submitter_id)
        return Creator(
                identifier = dto.submitter_id,
                name = dto.submitter_name,
                creationDate = userDto?.creation_date,
                //TODO support user image from db!
                image = null
        )
    }
}