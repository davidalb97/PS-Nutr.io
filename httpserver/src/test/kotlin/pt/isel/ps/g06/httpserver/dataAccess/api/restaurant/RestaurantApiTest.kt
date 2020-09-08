package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import pt.isel.ps.g06.httpserver.anyNonNull
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.parameterizedMock
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class RestaurantApiTest {
    private lateinit var httpClient: HttpClient
    private lateinit var restaurantUri: RestaurantUri
    private lateinit var responseMapper: ObjectMapper

    @BeforeEach
    fun mockDependencies() {
        httpClient = mock(HttpClient::class.java)
        restaurantUri = mock(RestaurantUri::class.java)
        responseMapper = mock(ObjectMapper::class.java)
    }

    @Test
    fun `searching nearby restaurants should only trigger a http request when a terminal operation is performed`() {
        //Setup
        `when`(restaurantUri.nearbyRestaurants(
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull()
        )).thenReturn(mock(URI::class.java))

        `when`(httpClient.send<String>(anyNonNull(), anyNonNull())).thenReturn(parameterizedMock())

        val api = RestaurantApiMock(httpClient, restaurantUri, responseMapper)

        //Act
        val nearbyRestaurants = api.searchNearbyRestaurants(
                latitude = 10F,
                longitude = 10F,
                radiusMeters = 42,
                name = "Some name",
                skip = 0,
                count = 10
        )

        //Not performing a terminal operation should not send the HTTP request
        nearbyRestaurants.map { "Some restaurant" }

        verifyNoInteractions(restaurantUri)
        verifyNoInteractions(httpClient)
        verifyNoInteractions(responseMapper)

        //Perform a terminal operation
        nearbyRestaurants.toList()

        verify(restaurantUri).nearbyRestaurants(anyNonNull(), anyNonNull(), anyNonNull(), anyNonNull(), anyNonNull(), anyNonNull())
        verify(httpClient).send<String>(anyNonNull(), anyNonNull())
    }
}

internal class RestaurantApiMock(client: HttpClient, uri: RestaurantUri, mapper: ObjectMapper) : RestaurantApi(client, uri, mapper) {
    override fun handleRestaurantInfoResponse(response: HttpResponse<String>): RestaurantDto? {
        throw NotImplementedError()
    }

    override fun handleNearbyRestaurantsResponse(response: HttpResponse<String>): Collection<RestaurantDto> {
        val restaurant = mock(RestaurantDto::class.java)
        return listOf(restaurant, restaurant, restaurant)
    }

    override fun buildGetRequest(uri: URI): HttpRequest {
        return mock(HttpRequest::class.java)
    }
}