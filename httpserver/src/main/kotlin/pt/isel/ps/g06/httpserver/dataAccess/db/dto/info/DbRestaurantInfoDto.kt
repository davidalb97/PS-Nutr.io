package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

class DbRestaurantInfoDto(
        val submission_id: Int,
        restaurant_name: String,
        latitude: Float,
        longitude: Float,
        val positiveVotes: Int,
        val negativeVotes: Int
) : RestaurantDto(
        submission_id.toString(),
        restaurant_name,
        latitude,
        longitude
)