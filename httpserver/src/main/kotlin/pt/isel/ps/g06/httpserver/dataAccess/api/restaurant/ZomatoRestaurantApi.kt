package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri.ZomatoUriBuilder
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"
private const val USER_KEY = "user-key"

@Repository
class ZomatoRestaurantApi(
        httpClient: HttpClient,
        uriBuilder: ZomatoUriBuilder,
        responseMapper: ObjectMapper
) : RestaurantApi(httpClient, uriBuilder, responseMapper) {
    override fun handleRestaurantInfoResponse(response: HttpResponse<String>): RestaurantDto? {
        TODO("Not yet implemented")
    }

    override fun handleNearbyRestaurantsResponse(response: HttpResponse<String>): Collection<RestaurantDto> {
        TODO("Not yet implemented")
    }

    override fun searchRestaurantsByName(name: String, countryCode: String): Collection<RestaurantDto> {
        TODO("Not yet implemented")
    }

    override fun buildGetRequest(uri: URI): HttpRequest {
        return HttpRequest
                .newBuilder(uri)
                .GET()
                .header("accept", MediaType.APPLICATION_JSON_VALUE)
                .header(USER_KEY, ZOMATO_API_KEY)
                .build()
    }
}
