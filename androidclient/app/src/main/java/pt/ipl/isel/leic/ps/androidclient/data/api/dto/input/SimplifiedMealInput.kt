package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import java.net.URI

open class SimplifiedMealInput(
    val id: Int,
    val name: String,
    val image: URI?,
    val votes: VotesInputDto?,
    val isFavorite: Boolean,
    val portions: Iterable<>
)