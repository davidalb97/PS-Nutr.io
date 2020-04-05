package pt.isel.ps.g06.httpserver.dataAccess.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import pt.isel.ps.g06.httpserver.dataAccess.ApiRequester
import pt.isel.ps.g06.httpserver.dataAccess.api.dtos.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.api.dtos.RestaurantSearchResultDto
import java.net.http.HttpClient
import java.util.concurrent.CompletableFuture

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"


class ZomatoApi(private val requester: ApiRequester, private val jsonMapper: ObjectMapper) {

    fun searchRestaurants(latitude: Float, longitude: Float, radius: Int): CompletableFuture<RestaurantSearchResultDto> {
        //TODO - Check errors
        return requester.request(
                search(latitude, longitude, radius),
                mapOf(Pair("user-key", ZOMATO_API_KEY), Pair("Accept", "application/json")),
                { false },
                { jsonMapper.readValue(it.body(), RestaurantSearchResultDto::class.java) }
        )
    }

    fun restaurantDailyMeals(restaurantId: Int): CompletableFuture<DailyMenuDto> {
        return requester.request(
                dailyMenu(restaurantId),
                mapOf(Pair("user-key", ZOMATO_API_KEY), Pair("Accept", "application/json")),
                { false },
                { jsonMapper.readValue(it.body(), DailyMenuDto::class.java) }
        )
    }
}
