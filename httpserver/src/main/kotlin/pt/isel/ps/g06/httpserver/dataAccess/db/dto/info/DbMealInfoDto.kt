package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto


open class DbMealInfoDto(
        val submission_id: Int,
        val meal_name: String,
        val positiveVotes: Int,
        val negativeVotes: Int
)