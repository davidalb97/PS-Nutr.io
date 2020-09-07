package pt.isel.ps.g06.httpserver.dataAccess.model.mapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.dataAccess.model.mapper.ResponseMapper
import pt.isel.ps.g06.httpserver.model.Votes

@Component
class DbVotesResponseMapper : ResponseMapper<DbVotesDto, Votes> {
    override fun mapTo(dto: DbVotesDto): Votes {
        return Votes(positive = dto.positive_count, negative = dto.negative_count)
    }
}