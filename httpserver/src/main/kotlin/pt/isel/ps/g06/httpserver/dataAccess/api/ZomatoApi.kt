package pt.isel.ps.g06.httpserver.dataAccess.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import pt.isel.ps.g06.httpserver.dataAccess.api.dtos.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.api.dtos.RestaurantSearchResultDto
import pt.isel.ps.g06.httpserver.util.log
import java.lang.RuntimeException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"


class ZomatoApi(httpClient: HttpClient, jsonMapper: ObjectMapper) : BaseApiRequester(httpClient, jsonMapper) {

    private fun <D : Any> request(uriStr: String, dtoClass: Class<D>): D {
        val request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStr))
                .header("user-key", ZOMATO_API_KEY)
                .header("Accept", "application/json")
                .build()

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                //TODO Proper error function
                //.thenApply { if (it.statusCode() == 200) return@thenApply it else throw Exception() }
                //.thenApply { jsonMapper.readValue(it.body(), dtoClass) }
                .thenApply {
                    if (it.statusCode() != 200)
                        log(RuntimeException())
                    jsonMapper.readValue(it.body(), dtoClass)
                }.get()
    }

    fun searchRestaurants(latitude: Float, longitude: Float, radius: Int): RestaurantSearchResultDto {
        val uriStr: String = search(latitude, longitude, radius)
        return request(uriStr, RestaurantSearchResultDto::class.java)
    }

    fun restaurantDailyMeals(restaurantId: Int): DailyMenuDto {
        val uriStr: String = dailyMeals(restaurantId)
        return request(uriStr, DailyMenuDto::class.java)
    }
}

fun main() {
    val client = HttpClient.newBuilder().build()

    val objMapper = ObjectMapper()
            //Ignore unknown json fields
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            //Enable primary constructor on DTOs
            .registerModule(KotlinModule())

    val api = ZomatoApi(client, objMapper)

    api.searchRestaurants(38.7337F, -9.1448F, 1000)
            .restaurants.forEach { println(it) }

    val restaurantDailyMeals = api.restaurantDailyMeals(1)
    restaurantDailyMeals.daily_menu?.forEach { println(it) }
}
