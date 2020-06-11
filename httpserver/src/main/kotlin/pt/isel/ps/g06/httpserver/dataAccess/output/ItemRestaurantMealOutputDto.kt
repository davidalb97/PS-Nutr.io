package pt.isel.ps.g06.httpserver.dataAccess.output

data class ItemRestaurantMealOutputDto(
        val mealItem: ItemMealOutputDto,
        val votes: VotesOutputDto?,
        val userVote: Boolean?
)