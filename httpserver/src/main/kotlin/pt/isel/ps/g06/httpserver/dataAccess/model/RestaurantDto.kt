package pt.isel.ps.g06.httpserver.dataAccess.model

import java.net.URI

open class RestaurantDto(
        val id: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val image: URI?
)