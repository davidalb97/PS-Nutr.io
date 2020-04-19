package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto

import pt.isel.ps.g06.httpserver.dataAccess.RestaurantDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantResponse

data class RestaurantSearchResultDtoMapper(val restaurants: Array<RestaurantContainerDto>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantSearchResultDtoMapper

        if (!restaurants.contentEquals(other.restaurants)) return false

        return true
    }

    override fun hashCode(): Int {
        return restaurants.contentHashCode()
    }
}

data class RestaurantContainerDto(val restaurant: RestaurantDto)

data class RestaurantDto(val id: Int, val name: String, val url: String, val cuisines: String, val location: Location) : RestaurantDtoMapper() {
    override fun mapDto(): RestaurantResponse {
        return RestaurantResponse(
                id,
                name,
                location.latitude,
                location.longitude,
                cuisines.split(", ").toTypedArray()
        )
    }
}

data class Location(val address: String, val locality: String, val city: String, val city_id: Int, val latitude: Float, val longitude: Float)


