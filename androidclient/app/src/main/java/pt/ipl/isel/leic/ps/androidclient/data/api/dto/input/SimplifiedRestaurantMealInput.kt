package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import java.net.URI

open class SimplifiedRestaurantMealInput(
    val id: Int,
    val name: String,
    val votes: VotesInputDto?,
    val isFavorite: Boolean?,
    val image: URI?
)