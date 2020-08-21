package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereErrorDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultContainer
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.exception.HereBadGatewayException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.exception.HereBadRequestException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri.HereUriBuilder
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

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
//            HttpStatus.BAD_REQUEST.value() -> throw mapToBadRequest(body)
                else -> throw mapToBadGateway(body)
            }
        }

    }

    override fun handleNearbyRestaurantsResponse(responseFuture: CompletableFuture<HttpResponse<String>>): CompletableFuture<Collection<RestaurantDto>> {
        return responseFuture.thenApply { response ->
            val body = response.body()

            return@thenApply when (response.statusCode()) {
                HttpStatus.OK.value() -> mapToNearbyRestaurants(body)
                HttpStatus.NOT_FOUND.value() -> emptyList()
                HttpStatus.BAD_REQUEST.value() -> throw mapToBadRequest(body)
                else -> throw mapToBadGateway(body)
            }
        }
    }

    override fun buildGetRequest(uri: URI): HttpRequest {
        return HttpRequest
                .newBuilder(uri)
                .GET()
                .build()
    }

    private fun mapToRestaurantDto(body: String?): RestaurantDto? {
        return responseMapper.readValue(body, HereResultItem::class.java)
    }

    private fun mapToNearbyRestaurants(body: String?): Collection<RestaurantDto> {
        return responseMapper.readValue(body, HereResultContainer::class.java).items
    }

    private fun mapToBadGateway(body: String?): Throwable {
        val error = responseMapper.readValue(body, HereErrorDto::class.java)
        throw HereBadGatewayException(error)
    }

    private fun mapToBadRequest(body: String?): Throwable {
        val error = responseMapper.readValue(body, HereErrorDto::class.java)
        throw HereBadRequestException(error)
    }
}