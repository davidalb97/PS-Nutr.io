package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.submitter

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.model.Submitter

@Component
class DbSubmitterResponseMapper(
        private val apiSubmitterMapper: ApiSubmitterMapper
) : ResponseMapper<DbSubmitterDto, Submitter> {
    override fun mapTo(dto: DbSubmitterDto): Submitter {
        return Submitter(
                identifier = dto.submitter_id,
                isUser = apiSubmitterMapper.getApiType(dto.submitter_id) == null,
                creationDate = dto.creation_date,
                image = null
        )
    }
}