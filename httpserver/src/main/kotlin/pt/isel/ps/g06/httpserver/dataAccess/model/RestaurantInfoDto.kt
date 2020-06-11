package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.model.RestaurantItem
import pt.isel.ps.g06.httpserver.model.Votes

open class RestaurantInfoDto(
        id: String,
        name: String,
        latitude: Float,
        longitude: Float,
        votes: Votes,
        userVote: Boolean?,
        isFavorite: Boolean
): RestaurantItemDto(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        votes = votes,
        userVote = userVote,
        isFavorite = isFavorite
)