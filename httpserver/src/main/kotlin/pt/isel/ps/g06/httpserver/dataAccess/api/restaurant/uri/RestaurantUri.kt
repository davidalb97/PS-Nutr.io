package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri

import org.springframework.stereotype.Component
import java.net.URI

@Component
interface RestaurantUri {
    fun nearbyRestaurants(
            latitude: Float,
            longitude: Float,
            radius: Int,
            restaurantName: String? = null,
            skip: Int?,
            count: Int?
    ): URI

    fun getRestaurantInfo(restaurantId: String): URI
}