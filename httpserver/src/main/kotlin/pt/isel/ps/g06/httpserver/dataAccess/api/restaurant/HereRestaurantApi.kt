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

@Repository
class HereRestaurantApi(
        httpClient: HttpClient,
        uriBuilder: HereUriBuilder,
        responseMapper: ObjectMapper
) : RestaurantApi(httpClient, uriBuilder, responseMapper) {

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