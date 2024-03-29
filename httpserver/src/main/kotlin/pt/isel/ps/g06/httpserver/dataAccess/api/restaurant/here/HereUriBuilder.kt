package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantUri
import java.net.URI

private const val DISCOVER_PATH = "https://discover.search.hereapi.com/v1/discover"
private const val LOOKUP_PATH = "https://lookup.search.hereapi.com/v1/lookup"

private const val KEY = "G8OIAAXhyb2kdJQrmVfeXpuSWmIVEFjjLQNXf3fr2v4"
private const val SEARCH_RESTAURANTS = "restaurant"
private const val MAX_LIMIT = 100

//List of query params
private const val IN = "in"
private const val IN_CIRCLE = "circle:"
private const val RADIUS = "r"
private const val QUERY = "q"
private const val LIMIT = "limit"
private const val ID = "id"
private const val API_KEY = "apiKey"


@Component
class HereUriBuilder : RestaurantUri {
    override fun nearbyRestaurants(
            latitude: Float,
            longitude: Float,
            radius: Int,
            restaurantName: String?,
            skip: Int?,
            count: Int?
    ): URI {
        return UriComponentsBuilder
                .fromHttpUrl(DISCOVER_PATH)
                .queryParam(IN, nearbyCircleByGeocode(latitude, longitude, radius))
                .queryParam(QUERY, queryRestaurantName(restaurantName))
                .queryParam(LIMIT, count)
                .queryParam(API_KEY, KEY)
                .build()
                .toUri()
    }

    override fun getRestaurantInfo(restaurantId: String): URI {
        return UriComponentsBuilder
                .fromHttpUrl(LOOKUP_PATH)
                .queryParam(ID, restaurantId)
                .queryParam(API_KEY, KEY)
                .build()
                .toUri()
    }

    private fun nearbyCircleByGeocode(latitude: Float, longitude: Float, radius: Int): String {
        return "$IN_CIRCLE$latitude,$longitude;$RADIUS=$radius"
    }

    private fun queryRestaurantName(restaurantName: String?): String {
        return if (restaurantName == null) SEARCH_RESTAURANTS
        else "$SEARCH_RESTAURANTS,$restaurantName"
    }
}