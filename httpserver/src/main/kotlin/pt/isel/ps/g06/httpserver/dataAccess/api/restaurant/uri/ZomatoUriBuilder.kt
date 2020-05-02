package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

private const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"
private const val ZOMATO_SEARCH_RESTAURANT = "${ZOMATO_BASE_URL}restaurant?"
private const val ZOMATO_SEARCH_URL = "${ZOMATO_BASE_URL}search?"
private const val ZOMATO_DAILY_MEALS_URL = "${ZOMATO_BASE_URL}dailymenu?"

private const val MAX_RANGE = 100
private const val MAX_RESULT_COUNT = 30

@Component
class ZomatoUriBuilder {

    fun restaurantSearchUri(latitude: Float?, longitude: Float?, radius: Int? = MAX_RANGE, count: Int = MAX_RESULT_COUNT): URI {
        return UriComponentsBuilder
                .fromPath(ZOMATO_SEARCH_URL)
                .queryParam("count", count)
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("radius", radius)
                .build()
                .toUri()
    }

    fun searchRestaurantById(restaurantId: Int): URI {
        return UriComponentsBuilder
                .fromPath(ZOMATO_SEARCH_RESTAURANT)
                .queryParam("res_id", restaurantId)
                .build()
                .toUri()
    }

    fun restaurantDailyMenuUri(restaurantId: Int): URI {
        return UriComponentsBuilder
                .fromPath(ZOMATO_DAILY_MEALS_URL)
                .queryParam("res_id", restaurantId).build().toUri()
    }
}