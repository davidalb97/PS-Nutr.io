package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

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

data class RestaurantContainerDto(val restaurant: ApiRestaurantDto)

class ApiRestaurantDto(
        id: Int,
        name: String,
        url: String,
        cuisines: String,
        location: Location
) : RestaurantDto(
        id,
        name,
        location.latitude,
        location.longitude,
        cuisines.split(", ").toList()
)

data class Location(val address: String, val locality: String, val city: String, val city_id: Int, val latitude: Float, val longitude: Float)


