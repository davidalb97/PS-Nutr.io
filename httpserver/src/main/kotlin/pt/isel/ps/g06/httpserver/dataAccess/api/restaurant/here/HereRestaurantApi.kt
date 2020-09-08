package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.dto.HereErrorDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.dto.HereResultContainer
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.dto.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.exception.problemJson.badGateway.HereBadGatewayException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class HereRestaurantApi(
        httpClient: HttpClient,
        uriBuilder: HereUriBuilder,
        objectMapper: ObjectMapper
) : RestaurantApi(httpClient, uriBuilder, objectMapper) {

    override fun handleRestaurantInfoResponse(response: HttpResponse<String>): RestaurantDto? {
        val body = response.body()

        return when (response.statusCode()) {
            HttpStatus.OK.value() -> mapToRestaurantDto(body)
            HttpStatus.BAD_REQUEST.value() -> null
            HttpStatus.NOT_FOUND.value() -> null
            else -> throw mapApiException(body)
        }
    }

    override fun handleNearbyRestaurantsResponse(response: HttpResponse<String>): Collection<RestaurantDto> {
        val body = response.body()

        return when (response.statusCode()) {
            HttpStatus.OK.value() -> mapToNearbyRestaurants(body)
            HttpStatus.NOT_FOUND.value() -> emptyList()
            HttpStatus.BAD_REQUEST.value() -> throw mapApiException(body)
            else -> throw mapApiException(body)
        }
    }


    override fun buildGetRequest(uri: URI): HttpRequest {
        return HttpRequest
                .newBuilder(uri)
                .GET()
                .build()
    }

    private fun mapToRestaurantDto(body: String?): RestaurantDto? {
        return objectMapper.readValue(body, HereResultItem::class.java)
    }

    private fun mapToNearbyRestaurants(body: String?): Collection<RestaurantDto> {
        return objectMapper.readValue(body, HereResultContainer::class.java).items
    }

    private fun mapApiException(body: String?): Throwable {
        val error = objectMapper.readValue(body, HereErrorDto::class.java)
        throw HereBadGatewayException(error)
    }
}