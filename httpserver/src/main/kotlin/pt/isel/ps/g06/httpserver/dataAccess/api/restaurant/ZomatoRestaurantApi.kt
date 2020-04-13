package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.HttpApiClient
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.RestaurantSearchResultDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantResponse
import java.util.concurrent.CompletableFuture

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"

class ZomatoRestaurantApi(private val clientHttp: HttpApiClient, private val jsonMapper: ObjectMapper) : IRestaurantApi {

    private val uri = ZomatoUriBuilder()

    override fun getRestaurantInfo(id: Int): Any {
        TODO("Not yet implemented")
    }

    override fun searchRestaurants(
            latitude: Float,
            longitude: Float,
            radiusMeters: Int
    ): CompletableFuture<List<RestaurantResponse>> {
        return requestDto<RestaurantSearchResultDto>(uri.restaurantSearchUri(latitude, longitude, radiusMeters))
                .thenApply(RestaurantSearchResultDto::unDto)
    }

    override fun restaurantDailyMeals(restaurantId: Int): CompletableFuture<List<String>> {
        return requestDto<DailyMenuDto>(uri.restaurantDailyMenuUri(restaurantId))
                .thenApply(DailyMenuDto::unDto)
    }

    private fun <D> requestDto(urlStr: String): CompletableFuture<D> {
        return clientHttp.request(
                urlStr,
                mapOf(Pair("user-key", ZOMATO_API_KEY), Pair("Accept", "application/json")),
                { false },
                { jsonMapper.readValue(it.body(), object : TypeReference<D>() {}) }
        )
    }

    override fun getType() = RestaurantApiType.Zomato
}
