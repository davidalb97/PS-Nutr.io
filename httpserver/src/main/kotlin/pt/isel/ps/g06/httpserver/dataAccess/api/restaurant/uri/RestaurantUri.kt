package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri

import java.net.URI

interface RestaurantUri {
    fun nearbyRestaurants(
            latitude: Float,
            longitude: Float,
            radius: Int,
            restaurantName: String? = null,
            limit: Int? = null
    ): URI

    fun getRestaurantInfo(restaurantId: Int): URI
}