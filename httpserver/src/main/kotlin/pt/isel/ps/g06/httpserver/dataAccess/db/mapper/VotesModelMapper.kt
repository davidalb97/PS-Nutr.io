package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.model.Votes

@Component
class DbVotesModelMapper : ModelMapper<DbVotesDto, Votes> {
    override fun mapTo(dto: DbVotesDto): Votes {
        return Votes(positive = dto.positive_count, negative = dto.negative_count)
    }
}