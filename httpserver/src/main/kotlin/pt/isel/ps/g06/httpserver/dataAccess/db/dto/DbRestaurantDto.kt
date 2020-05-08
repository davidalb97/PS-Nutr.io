package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

class DbRestaurantDto(
        submission_id: String,
        restaurant_name: String,
        latitude: Float,
        longitude: Float
) : RestaurantDto(
        submission_id,
        restaurant_name,
        latitude,
        longitude
)