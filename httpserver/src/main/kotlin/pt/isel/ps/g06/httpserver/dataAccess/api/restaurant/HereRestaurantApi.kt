package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereErrorDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultContainer
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri.HereUriBuilder
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.exception.problemJson.badGateway.HereBadGatewayException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

private const val HERE_API_MAX_COUNT = 100

@Repository
class HereRestaurantApi(
        httpClient: HttpClient,
        uriBuilder: HereUriBuilder,
        responseMapper: ObjectMapper
) : RestaurantApi(httpClient, uriBuilder, responseMapper) {

    override fun handleRestaurantInfoResponse(responseFuture: CompletableFuture<HttpResponse<String>>): CompletableFuture<RestaurantDto?> {
        return responseFuture.thenApply { response ->
            val body = response.body()

            return@thenApply when (response.statusCode()) {
                HttpStatus.OK.value() -> mapToRestaurantDto(body)
                HttpStatus.BAD_REQUEST.value() -> null
                HttpStatus.NOT_FOUND.value() -> null
                else -> throw mapApiException(body)
            }
        }

    }

    override fun handleNearbyRestaurantsResponse(responseFuture: CompletableFuture<HttpResponse<String>>): CompletableFuture<Collection<RestaurantDto>> {
        return responseFuture.thenApply { response ->
            val body = response.body()

            return@thenApply when (response.statusCode()) {
                HttpStatus.OK.value() -> mapToNearbyRestaurants(body)
                HttpStatus.NOT_FOUND.value() -> emptyList()
                HttpStatus.BAD_REQUEST.value() -> throw mapApiException(body)
                else -> throw mapApiException(body)
            }
        }
    }

    override fun buildGetRequest(uri: URI): HttpRequest {
        return HttpRequest
                .newBuilder(uri)
                .GET()
                .build()
    }

    override fun searchNearbyRestaurants(
            latitude: Float,
            longitude: Float,
            radiusMeters: Int,
            name: String?,
            skip: Int?,
            count: Int
    ): CompletableFuture<Collection<RestaurantDto>> {
        val apiCount = if (skip == null || skip == 0) count else count.plus(count.times(skip))
        return if (apiCount <= HERE_API_MAX_COUNT) {
            super.searchNearbyRestaurants(latitude, longitude, radiusMeters, name, skip, apiCount)
                    .thenApply { it.drop(apiCount - count) }
        } else {
            CompletableFuture.completedFuture(emptyList())
        }
    }

    private fun mapToRestaurantDto(body: String?): RestaurantDto? {
        return responseMapper.readValue(body, HereResultItem::class.java)
    }

    private fun mapToNearbyRestaurants(body: String?): Collection<RestaurantDto> {
        return responseMapper.readValue(body, HereResultContainer::class.java).items
    }

    private fun mapApiException(body: String?): Throwable {
        val error = responseMapper.readValue(body, HereErrorDto::class.java)
        throw HereBadGatewayException(error)
    }
}