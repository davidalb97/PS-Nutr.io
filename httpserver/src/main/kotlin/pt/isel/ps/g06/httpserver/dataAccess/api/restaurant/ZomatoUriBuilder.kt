package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.api.AUriBuilder

private const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"
private const val ZOMATO_SEARCH_URL = "${ZOMATO_BASE_URL}search?"
private const val ZOMATO_DAILY_MEALS_URL = "${ZOMATO_BASE_URL}dailymenu?"

private const val MAX_RANGE = 100
private const val MAX_RESULT_COUNT = 30

class ZomatoUriBuilder : AUriBuilder() {

    fun restaurantSearchUri(latitude: Float?, longitude: Float?, radius: Int? = MAX_RANGE, count: Int = MAX_RESULT_COUNT): String {
        return ZOMATO_SEARCH_URL +
                "count=$count" +
                param("lat", latitude) +
                param("lon", longitude) +
                param("radius", radius)
    }

    fun restaurantDailyMenuUri(restaurantId: Int) = "${ZOMATO_DAILY_MEALS_URL}res_id=$restaurantId"
}