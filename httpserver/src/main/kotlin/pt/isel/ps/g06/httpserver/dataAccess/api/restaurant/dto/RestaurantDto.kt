package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto

import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantResponse

data class RestaurantSearchResultDto(val restaurants: Array<RestaurantContainerDto>) : IUnDto<List<RestaurantResponse>> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantSearchResultDto

        if (!restaurants.contentEquals(other.restaurants)) return false

        return true
    }

    override fun hashCode(): Int {
        return restaurants.contentHashCode()
    }

    override fun unDto(): List<RestaurantResponse> {
        return restaurants.map { container ->
            container.restaurant.let {
                RestaurantResponse(
                        it.id,
                        RestaurantApiType.Zomato,
                        it.name,
                        it.location.latitude,
                        it.location.longitude,
                        it.cuisines.split(", ").toTypedArray()
                )
            }
        }
    }
}

data class RestaurantContainerDto(val restaurant: RestaurantDto)

data class RestaurantDto(val id: Int, val name: String, val url: String, val cuisines: String, val location: Location)

data class Location(val address: String, val locality: String, val city: String, val city_id: Int, val latitude: Float, val longitude: Float)


