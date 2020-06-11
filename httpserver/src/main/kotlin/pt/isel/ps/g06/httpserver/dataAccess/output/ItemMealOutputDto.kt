package pt.isel.ps.g06.httpserver.dataAccess.output

data class ItemMealOutputDto(
        val id: Int,
        val name: String,
        val image: String,
        val user: UserOutputDto,
        val votes: VotesOutputDto
)