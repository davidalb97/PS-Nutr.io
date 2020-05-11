package pt.isel.ps.g06.httpserver.dataAccess.model

open class RestaurantDto(
        val id: String,
        val name: String,
        val latitude: Float,
        val longitude: Float
)