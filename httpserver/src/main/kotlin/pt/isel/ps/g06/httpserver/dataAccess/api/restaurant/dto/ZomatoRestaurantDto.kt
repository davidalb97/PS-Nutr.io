package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantItemDto
import pt.isel.ps.g06.httpserver.model.Votes

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
) : RestaurantInfoDto(
        id,
        name,
        location.latitude,
        location.longitude,
        //There are no votes if it's not inserted on db yet
        votes = Votes(0, 0),
        //User has not voted yet if not inserted
        userVote = null,
        //User has not favored yet if not inserted
        isFavorite = false
)

data class Location(val address: String, val locality: String, val city: String, val city_id: Int, val latitude: Float, val longitude: Float)


