package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.common.BaseApiRequester
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri.RestaurantUri
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantItemDto
import pt.isel.ps.g06.httpserver.model.RestaurantInfo
import pt.isel.ps.g06.httpserver.model.RestaurantItem
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Repository
abstract class RestaurantApi(
        httpClient: HttpClient = HttpClient.newHttpClient(),
        private val restaurantUri: RestaurantUri,
        responseMapper: ObjectMapper
) : BaseApiRequester(httpClient, responseMapper) {

    fun getRestaurantInfo(id: String): CompletableFuture<RestaurantInfoDto?> {
        val uri = restaurantUri.getRestaurantInfo(id)
        val response = httpClient.sendAsync(buildGetRequest(uri), HttpResponse.BodyHandlers.ofString())
        return handleRestaurantInfoResponse(response)
    }

    fun searchNearbyRestaurants(latitude: Float, longitude: Float, radiusMeters: Int, name: String?): CompletableFuture<Collection<RestaurantItemDto>> {
        val uri = restaurantUri.nearbyRestaurants(latitude, longitude, radiusMeters, name)
        val response = httpClient.sendAsync(buildGetRequest(uri), HttpResponse.BodyHandlers.ofString())
        return handleNearbyRestaurantsResponse(response)
    }

    abstract fun handleRestaurantInfoResponse(responseFuture: CompletableFuture<HttpResponse<String>>): CompletableFuture<RestaurantInfoDto?>

    abstract fun handleNearbyRestaurantsResponse(responseFuture: CompletableFuture<HttpResponse<String>>): CompletableFuture<Collection<RestaurantItemDto>>

    abstract fun buildGetRequest(uri: URI): HttpRequest
}