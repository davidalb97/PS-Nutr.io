package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.common.BaseApiRequester
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri.RestaurantUri
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
abstract class RestaurantApi(
        httpClient: HttpClient = HttpClient.newHttpClient(),
        private val restaurantUri: RestaurantUri,
        responseMapper: ObjectMapper
) : BaseApiRequester(httpClient, responseMapper) {

    fun getRestaurantInfo(id: String): RestaurantDto? {
        val uri = restaurantUri.getRestaurantInfo(id)
        val response = httpClient.send(buildGetRequest(uri), HttpResponse.BodyHandlers.ofString())
        return handleRestaurantInfoResponse(response)
    }

    fun searchNearbyRestaurants(latitude: Float, longitude: Float, radiusMeters: Int, name: String?): Collection<RestaurantDto> {
        val uri = restaurantUri.nearbyRestaurants(latitude, longitude, radiusMeters, name)
        val response = httpClient.send(buildGetRequest(uri), HttpResponse.BodyHandlers.ofString())
        return handleNearbyRestaurantsResponse(response)
    }

    abstract fun handleRestaurantInfoResponse(response: HttpResponse<String>): RestaurantDto?

    abstract fun handleNearbyRestaurantsResponse(response: HttpResponse<String>): Collection<RestaurantDto>

    abstract fun buildGetRequest(uri: URI): HttpRequest
}