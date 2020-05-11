package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.RestaurantSearchResultDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoErrorDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.exception.ZomatoBadGatewayException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.exception.ZomatoBadRequestException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri.ZomatoUriBuilder
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"
private const val USER_KEY = "user-key"
private const val ACCEPT = "accept"

@Repository
class ZomatoRestaurantApi(
        httpClient: HttpClient,
        uriBuilder: ZomatoUriBuilder,
        responseMapper: ObjectMapper
) : RestaurantApi(httpClient, uriBuilder, responseMapper) {
    override fun handleRestaurantInfoResponse(response: HttpResponse<String>): RestaurantDto? {
        val body = response.body()

        return when (response.statusCode()) {
            HttpStatus.OK.value() -> mapToRestaurantDto(body)
            HttpStatus.BAD_REQUEST.value() -> handleBadRequest(body).let { null }
            else -> throw handleBadGateway(body)
        }
    }

    override fun handleNearbyRestaurantsResponse(response: HttpResponse<String>): Collection<RestaurantDto> {
        val body = response.body()

        return when (response.statusCode()) {
            HttpStatus.OK.value() -> mapToNearbyRestaurants(body)
            HttpStatus.BAD_REQUEST.value() -> handleBadRequest(body)
            else -> throw handleBadGateway(body)
        }
    }

    override fun buildGetRequest(uri: URI): HttpRequest {
        return HttpRequest
                .newBuilder(uri)
                .GET()
                .header(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(USER_KEY, ZOMATO_API_KEY)
                .build()
    }

    private fun mapToRestaurantDto(body: String?): RestaurantDto? {
        return responseMapper.readValue(body, ZomatoRestaurantDto::class.java)
    }

    private fun mapToNearbyRestaurants(body: String?): Collection<RestaurantDto> {
        return responseMapper.readValue(body, RestaurantSearchResultDtoMapper::class.java).restaurants.map { it.restaurant }
    }

    private fun handleBadRequest(body: String?): Collection<RestaurantDto> {
        val error = responseMapper.readValue(body, ZomatoErrorDto::class.java)

        if (error.code == HttpStatus.NOT_FOUND.value()) {
            return emptyList()
        } else {
            throw ZomatoBadRequestException(error)
        }
    }

    private fun handleBadGateway(body: String?): Throwable {
        val error = responseMapper.readValue(body, ZomatoErrorDto::class.java)
        throw ZomatoBadGatewayException(error)
    }
}
