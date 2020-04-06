package pt.isel.ps.g06.httpserver.dataAccess.restaurants.api

private const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"
private const val ZOMATO_SEARCH_URL = "${ZOMATO_BASE_URL}search?"
private const val ZOMATO_DAILY_MEALS_URL = "${ZOMATO_BASE_URL}dailymenu?"

private const val MAX_RANGE = 100
private const val MAX_RESULT_COUNT = 30

fun search(latitude: Float?, longitude: Float?, radius: Int? = MAX_RANGE, count: Int = MAX_RESULT_COUNT): String {
    return ZOMATO_SEARCH_URL +
            "count=$count" +
            (latitude?.let { "&lat=$it" } ?: "") +
            (longitude?.let { "&lon=$it" } ?: "") +
            (radius?.let { "&radius=$it" } ?: "")
}

fun dailyMenu(restaurantId: Int) = "${ZOMATO_DAILY_MEALS_URL}res_id=$restaurantId"