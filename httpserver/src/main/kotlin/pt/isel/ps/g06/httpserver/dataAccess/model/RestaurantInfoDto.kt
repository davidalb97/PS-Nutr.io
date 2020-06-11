package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.model.RestaurantItem
import pt.isel.ps.g06.httpserver.model.Votes
import java.time.OffsetDateTime

open class RestaurantInfoDto(
        id: String,
        name: String,
        latitude: Float,
        longitude: Float,
        votes: Votes,
        userVote: Boolean?,
        isFavorite: Boolean?,
        creationDate: OffsetDateTime
): RestaurantItemDto(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        votes = votes,
        userVote = userVote,
        isFavorite = isFavorite,
        creationDate = creationDate
)