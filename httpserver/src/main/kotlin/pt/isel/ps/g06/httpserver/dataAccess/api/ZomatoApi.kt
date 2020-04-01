package pt.isel.ps.g06.httpserver.dataAccess.api

import com.fasterxml.jackson.databind.ObjectMapper
import pt.isel.ps.g06.httpserver.dto.RestaurantContainer
import pt.isel.ps.g06.httpserver.dto.RestaurantDto
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

//TODO This shouldn't be here, for testing only
private const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"
private const val ZOMATO_SEARCH = "search"

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"


class ZomatoApi(httpClient: HttpClient, jsonMapper: ObjectMapper) : BaseApiRequester(httpClient, jsonMapper) {
    fun searchRestaurants(latitude: Float, longitude: Float) {
        val request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("${ZOMATO_BASE_URL}${ZOMATO_SEARCH}?lat=${latitude}&lon=${longitude}?radius=1000"))
                .header("user-key", ZOMATO_API_KEY)
                .header("Accept", "application/json")
                .build()

        val get = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply { if (it.statusCode() == 200) return@thenApply it else throw Exception() }
                .thenApply { jsonMapper.readValue(it.body(), RestaurantContainer::class.java) }
                .get()

        get.restaurants.forEach { _ -> println() }
    }
}

fun main() {
    val client = HttpClient.newBuilder().build()
    ZomatoApi(client, ObjectMapper()).searchRestaurants(38.7337F, -9.1448F)
}
