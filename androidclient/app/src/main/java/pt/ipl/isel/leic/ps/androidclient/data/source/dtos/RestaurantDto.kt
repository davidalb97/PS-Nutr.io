package pt.ipl.isel.leic.ps.androidclient.data.source.dtos

import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

data class RestaurantsDto(val restaurants: Array<RestaurantDto>): IUnDto<List<Restaurant>> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantsDto

        if (!restaurants.contentEquals(other.restaurants)) return false

        return true
    }

    override fun hashCode(): Int {
        return restaurants.contentHashCode()
    }

    override fun unDto(): List<Restaurant> = restaurants.map(RestaurantDto::unDto)
}

data class RestaurantDto(
        val apiId: Int,
        val apiType: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val cuisines: Array<String>
): IUnDto<Restaurant> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantDto

        if (apiId != other.apiId) return false
        if (apiType != other.apiType) return false
        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (!cuisines.contentEquals(other.cuisines)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = apiId
        result = 31 * result + apiType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + cuisines.contentHashCode()
        return result
    }

    override fun unDto(): Restaurant = Restaurant(
        apiId,
        apiType,
        name,
        latitude,
        longitude,
        cuisines
    )
}