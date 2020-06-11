package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto

data class DbPublicDto(
        val votes: DbVotesDto,
        val userVote: Boolean?
)