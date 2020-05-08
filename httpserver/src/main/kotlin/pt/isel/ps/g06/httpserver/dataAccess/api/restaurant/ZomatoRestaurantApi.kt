package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.common.BaseApi
import pt.isel.ps.g06.httpserver.dataAccess.api.common.HttpApiClient
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ApiRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.DailyMenuDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.RestaurantSearchResultDtoMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.ZomatoResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri.ZomatoUriBuilder
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"

@Repository
class ZomatoRestaurantApi(
        httpClient: HttpApiClient,
        private val uriBuilder: ZomatoUriBuilder,
        restaurantMapper: ZomatoResponseMapper
) : IRestaurantApi, BaseApi(httpClient, restaurantMapper) {
    private val headers = mapOf(Pair("user-key", ZOMATO_API_KEY), Pair("Accept", "application/json"))

    override fun getRestaurantInfo(id: String): RestaurantDto? {
        val uri = uriBuilder.searchRestaurantById(id)
        return requestDto(uri, ApiRestaurantDto::class.java, headers)
    }

    override fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): List<RestaurantDto> {
        val uri = uriBuilder.restaurantSearchUri(latitude, longitude, radiusMeters)
        return requestDto(uri, RestaurantSearchResultDtoMapper::class.java).restaurants.map { it.restaurant }
    }

    override fun restaurantDailyMeals(restaurantId: Int): List<String> {
        val uri = uriBuilder.restaurantDailyMenuUri(restaurantId)
        return requestDto(uri, DailyMenuDtoMapper::class.java, headers).let(DailyMenuDtoMapper::mapDto)
    }
}
