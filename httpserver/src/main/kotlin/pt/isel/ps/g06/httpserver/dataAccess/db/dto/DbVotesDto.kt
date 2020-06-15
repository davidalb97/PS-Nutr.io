package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import pt.isel.ps.g06.httpserver.dataAccess.model.VotesDto

class DbVotesDto(
        positive_count: Int,
        negative_count: Int
) : VotesDto(
        positive = positive_count,
        negative = negative_count
)