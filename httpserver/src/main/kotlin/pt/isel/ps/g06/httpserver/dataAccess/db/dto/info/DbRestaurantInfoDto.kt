package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

class DbRestaurantInfoDto(
        val submitterId: Int,
        val submission_id: Int,
        val apiId: Int?,
        restaurant_name: String,
        latitude: Float,
        longitude: Float,
        val positiveVotes: Int,
        val negativeVotes: Int,
        val vote: Boolean?
) : RestaurantDto(
        submission_id.toString(),
        restaurant_name,
        latitude,
        longitude
)