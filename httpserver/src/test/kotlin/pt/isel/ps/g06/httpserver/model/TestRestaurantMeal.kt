package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.*

data class TestRestaurantMeal(
        val submissionId: Int,
        val mealDto: DbMealDto,
        val restaurantDto: DbRestaurantDto,
        val portions: Collection<DbPortionDto>,
        val userVotes: Collection<DbUserVoteDto>,
        val votes: DbVotesDto
)