package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.model.Votes
import java.time.OffsetDateTime

open class RestaurantItemDto(
        val id: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val votes: Votes,
        val userVote: Boolean?,
        val isFavorite: Boolean?,
        val creationDate: OffsetDateTime
)