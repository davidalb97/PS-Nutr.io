package pt.isel.ps.g06.httpserver.dataAccess.restaurants.api.dtos


data class RestaurantSearchResultDto(val restaurants: Array<RestaurantContainerDto>) {

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
}

data class RestaurantContainerDto(val restaurant: RestaurantDto)

data class RestaurantDto(val id: Int, val name: String, val url: String, val cuisines: String, val location: Location)

data class Location(val address: String, val locality: String, val city: String, val city_id: Int, val latitude: Float, val longitude: Float)


