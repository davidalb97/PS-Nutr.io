package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.dataAccess.api.common.nonNullQueryParam
import java.net.URI

private const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"
private const val ZOMATO_SEARCH_RESTAURANT = "${ZOMATO_BASE_URL}restaurant"
private const val ZOMATO_SEARCH_URL = "${ZOMATO_BASE_URL}search"

private const val COUNT = "count"
private const val SKIP = "skip"
private const val LATITUDE = "lat"
private const val LONGITUDE = "lon"
private const val RADIUS = "radius"
private const val RESTAURANT_ID = "res_id"

@Component
class ZomatoUriBuilder : RestaurantUri {

    override fun nearbyRestaurants(
            latitude: Float,
            longitude: Float,
            radius: Int,
            restaurantName: String?,
            skip: Int?,
            count: Int?
    ): URI {
        return UriComponentsBuilder
                .fromHttpUrl(ZOMATO_SEARCH_URL)
                .queryParam(SKIP, skip)
                .queryParam(COUNT, count)
                .nonNullQueryParam(LATITUDE, latitude)
                .nonNullQueryParam(LONGITUDE, longitude)
                .nonNullQueryParam(RADIUS, radius)
                .build()
                .toUri()
    }

    override fun getRestaurantInfo(restaurantId: String): URI {
        return UriComponentsBuilder
                .fromHttpUrl(ZOMATO_SEARCH_RESTAURANT)
                .queryParam(RESTAURANT_ID, restaurantId)
                .build()
                .toUri()
    }
}