package pt.isel.ps.g06.httpserver.dataAccess.output

data class DetailedRestaurantMealOutputDto(
        val meal: DetailedMealOutputDto,
        val portions: Collection<Int>,
        val votes: VotesOutputDto
)