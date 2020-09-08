package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmitterType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.model.Submitter

@Component
class DbSubmitterModelMapper : ModelMapper<DbSubmitterDto, Submitter> {
    override fun mapTo(dto: DbSubmitterDto): Submitter {
        return Submitter(
                identifier = dto.submitter_id,
                creationDate = dto.creation_date,
                image = null,
                isUser = dto.submitter_type === SubmitterType.User.toString()
        )
    }
}