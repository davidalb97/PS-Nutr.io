package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.model.Votes

class DbVotesResponseMapper : ResponseMapper<DbVotesDto, Votes> {
    override fun mapTo(dto: DbVotesDto): Votes {
        return Votes(
                positive = dto.positive,
                negative = dto.negative
        )
    }
}