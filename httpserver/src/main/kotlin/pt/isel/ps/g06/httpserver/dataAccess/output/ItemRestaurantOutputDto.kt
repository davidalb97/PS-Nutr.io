package pt.isel.ps.g06.httpserver.dataAccess.output

data class ItemRestaurantOutputDto(
        val id: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val image: String,
        val votes: VotesOutputDto?,
        val userVote: Boolean?,
        val isFavorite: Boolean?
)