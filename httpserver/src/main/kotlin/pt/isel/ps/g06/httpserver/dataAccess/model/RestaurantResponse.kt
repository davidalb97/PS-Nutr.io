package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType

data class RestaurantResponse(
        val apiId: Int,
        val apiType: RestaurantApiType,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val cuisines: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantResponse

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
}