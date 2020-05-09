package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri

import java.net.URI

interface RestaurantUri {
    fun nearbyRestaurants(
            latitude: Float,
            longitude: Float,
            radius: Int,
            restaurantName: String? = null,
            count: Int? = null
    ): URI

    fun getRestaurantInfo(restaurantId: String): URI

    fun searchRestaurantsByName(name: String, countryCode: String, count: Int? = null): URI
}