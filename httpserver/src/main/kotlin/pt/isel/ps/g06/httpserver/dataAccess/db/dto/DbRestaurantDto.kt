package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantItemDto

open class DbRestaurantDto(
        val submission_id: Int,
        val restaurant_name: String,
        val latitude: Float,
        val longitude: Float
)