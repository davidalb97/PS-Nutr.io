package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

open class SimplifiedRestaurantInputDto(
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: VotesInputDto?,
    val isFavorite: Boolean?
)