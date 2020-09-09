package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.BaseApiRequester
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Repository
abstract class RestaurantApi(
        httpClient: HttpClient = HttpClient.newHttpClient(),
        protected val restaurantUri: RestaurantUri,
        objectMapper: ObjectMapper
) : BaseApiRequester(httpClient, objectMapper) {

    fun getRestaurantInfo(id: String): CompletableFuture<RestaurantDto?> {
        val uri = restaurantUri.getRestaurantInfo(id)

        return httpClient
                .sendAsync(buildGetRequest(uri), HttpResponse.BodyHandlers.ofString())
                .thenApply(::handleRestaurantInfoResponse)
    }

    fun searchNearbyRestaurants(
            latitude: Float,
            longitude: Float,
            radiusMeters: Int,
            name: String?,
            skip: Int?,
            count: Int
    ): Sequence<RestaurantDto> {
        /*
        Restaurant APIs dependencies do not offer pagination to obtain more restaurants when needed
        or skipping results. As such, generate sequence always only requests once
        and is unable to provide more when needed.

        (See: https://developer.here.com/documentation/geocoding-search-api/api-reference-swagger.html)
         */
        return sequence {
            val uri = restaurantUri.nearbyRestaurants(latitude, longitude, radiusMeters, name, skip, count)
            val response = httpClient.send(
                    buildGetRequest(uri),
                    HttpResponse.BodyHandlers.ofString()
            )

            yieldAll(handleNearbyRestaurantsResponse(response))
        }
    }

    abstract fun handleRestaurantInfoResponse(response: HttpResponse<String>): RestaurantDto?

    abstract fun handleNearbyRestaurantsResponse(response: HttpResponse<String>): Collection<RestaurantDto>
}