package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.model.Submitter
import java.time.OffsetDateTime
import java.time.ZoneId

@Component
class DbSubmitterResponseMapper(
        private val apiSubmitterMapper: ApiSubmitterMapper
) : ResponseMapper<DbSubmitterDto, Submitter> {
    override fun mapTo(dto: DbSubmitterDto): Submitter {
        return Submitter(
                identifier = dto.submitter_id,
                name = dto.submitter_name,
                isUser = apiSubmitterMapper.getApiType(dto.submitter_id) == null,
                creationDate = OffsetDateTime.ofInstant(dto.creation_date.toInstant(), ZoneId.systemDefault()),
                image = null
        )
    }
}