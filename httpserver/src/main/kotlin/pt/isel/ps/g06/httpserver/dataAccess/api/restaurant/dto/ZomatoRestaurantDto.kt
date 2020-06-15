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

data class RestaurantContainerDto(val restaurant: ZomatoRestaurantDto)

class ZomatoRestaurantDto(
        id: String,
        name: String,
        val cuisines: String,
        location: Location
) : RestaurantDto(
        id,
        name,
        location.latitude,
        location.longitude,
        //Zomato does not support image
        //TODO return restaurant image from Zomato result item
        image = null
)

data class Location(val address: String, val locality: String, val city: String, val city_id: Int, val latitude: Float, val longitude: Float)


