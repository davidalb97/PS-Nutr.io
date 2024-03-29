package pt.isel.ps.g06.httpserver.dataAccess.common.dto

import java.net.URI

open class RestaurantDto(
        val id: String,
        val name: String,
        val ownerId: Int?,
        val latitude: Float,
        val longitude: Float,
        val image: URI?
)