package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.dataAccess.api.common.nonNullQueryParam
import java.net.URI

private const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"
private const val ZOMATO_SEARCH_RESTAURANT = "${ZOMATO_BASE_URL}restaurant?"
private const val ZOMATO_SEARCH_URL = "${ZOMATO_BASE_URL}search?"
private const val ZOMATO_DAILY_MEALS_URL = "${ZOMATO_BASE_URL}dailymenu?"

private const val MAX_RANGE = 100
private const val MAX_RESULT_COUNT = 30

private const val COUNT = "count"
private const val LATITUDE = "lat"
private const val LONGITUDE = "lon"
private const val RADIUS = "radius"
private const val RESTAURANT_ID = "res_id"

@Component
class ZomatoUriBuilder {

    fun restaurantSearchUri(latitude: Float?, longitude: Float?, radius: Int? = MAX_RANGE, count: Int = MAX_RESULT_COUNT): URI {
        return UriComponentsBuilder
                .fromPath(ZOMATO_SEARCH_URL)
                .queryParam(COUNT, count)
                .nonNullQueryParam(LATITUDE, latitude)
                .nonNullQueryParam(LONGITUDE, longitude)
                .nonNullQueryParam(RADIUS, radius)
                .build()
                .toUri()
    }

    fun searchRestaurantById(restaurantId: Int): URI {
        return UriComponentsBuilder
                .fromPath(ZOMATO_SEARCH_RESTAURANT)
                .queryParam(RESTAURANT_ID, restaurantId)
                .build()
                .toUri()
    }

    fun restaurantDailyMenuUri(restaurantId: Int): URI {
        return UriComponentsBuilder
                .fromPath(ZOMATO_DAILY_MEALS_URL)
                .queryParam(RESTAURANT_ID, restaurantId).build().toUri()
    }
}