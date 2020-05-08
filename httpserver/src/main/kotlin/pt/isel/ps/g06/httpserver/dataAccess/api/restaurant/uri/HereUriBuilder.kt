package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

private const val DISCOVER_PATH = "https://discover.search.hereapi.com/v1/discover"

private const val KEY = "G8OIAAXhyb2kdJQrmVfeXpuSWmIVEFjjLQNXf3fr2v4"
private const val SEARCH_RESTAURANTS = "eat-drink"
private const val MAX_LIMIT = 100

//List of query params
private const val IN = "in"
private const val IN_CIRCLE = "circle:"
private const val RADIUS = "r"
private const val QUERY = "q"
private const val LIMIT = "limit"
private const val API_KEY = "apiKey"


@Component
class HereUriBuilder {
    fun nearbyRestaurants(
            latitude: Float,
            longitude: Float,
            radius: Int,
            restaurantName: String? = null,
            limit: Int? = null
    ): URI {
        return UriComponentsBuilder
                .fromPath(DISCOVER_PATH)
                .queryParam(IN, nearbyCircleByGeocode(latitude, longitude, radius))
                .queryParam(QUERY, SEARCH_RESTAURANTS, restaurantName)
                .queryParam(LIMIT, limit ?: MAX_LIMIT)
                .queryParam(API_KEY, KEY)
                .build()
                .toUri()
    }

    fun searchRestaurant(restaurantId: Int): URI {
        TODO("Not yet implemented")
    }

    private fun nearbyCircleByGeocode(latitude: Float, longitude: Float, radius: Int): String {
        return "$IN_CIRCLE$latitude,$longitude;$RADIUS=$radius"
    }
}