package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.HttpApiClient
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ApiRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.DailyMenuDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.RestaurantSearchResultDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"

class ZomatoRestaurantApi(private val clientHttp: HttpApiClient, private val jsonMapper: ObjectMapper) : IRestaurantApi {

    private val uriBuilder = ZomatoUriBuilder()

    override fun getRestaurantInfo(id: Int): RestaurantDto? {
        val uri = uriBuilder.searchRestaurantById(id)
        return requestDto(uri, ApiRestaurantDto::class.java)
    }

    override fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): List<RestaurantDto> {
        val uri = uriBuilder.restaurantSearchUri(latitude, longitude, radiusMeters)

        return requestDto(uri, RestaurantSearchResultDtoMapper::class.java).restaurants.map { it.restaurant }
    }

    override fun restaurantDailyMeals(restaurantId: Int): List<String> {
        return requestDto(uriBuilder.restaurantDailyMenuUri(restaurantId), DailyMenuDtoMapper::class.java).let(DailyMenuDtoMapper::mapDto)
    }

    private fun <D> requestDto(urlStr: String, klass: Class<D>): D {
        return clientHttp.request(
                urlStr,
                mapOf(Pair("user-key", ZOMATO_API_KEY), Pair("Accept", "application/json")),
                { false },
                { jsonMapper.readValue(it.body(), klass) }
        )
    }
}
