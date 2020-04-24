package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class RestaurantDto(
        val submission_id: Int,
        val restaurant_name: String,
        val latitude: Float,
        val longitude: Float
)