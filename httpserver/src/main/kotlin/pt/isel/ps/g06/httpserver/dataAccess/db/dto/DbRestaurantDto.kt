package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

class DbRestaurantDto(
        val submission_id: Int,
        restaurant_name: String,
        latitude: Float,
        longitude: Float
) : RestaurantDto(
        submission_id.toString(),
        restaurant_name,
        latitude,
        longitude
)