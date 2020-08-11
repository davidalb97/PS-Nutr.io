package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.submitter

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmitterType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.model.Submitter

@Component
class SubmitterResponseMapper : ResponseMapper<DbSubmitterDto, Submitter> {
    override fun mapTo(dto: DbSubmitterDto): Submitter {
        return Submitter(
                dto.submitter_id,
                dto.submitter_name,
                dto.creation_date,
                null,   //TODO When users have custom images
                dto.submitter_type === SubmitterType.User.toString()
        )
    }
}