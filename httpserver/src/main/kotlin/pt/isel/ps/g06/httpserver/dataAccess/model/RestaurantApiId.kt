package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType


data class RestaurantApiId(
     val id: Int,
     val apiType: RestaurantApiType
)