package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.HttpApiClient
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.IUnDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.RestaurantSearchResultDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantResponse
import java.util.concurrent.CompletableFuture

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"

class ZomatoRestaurantApi(private val clientHttp: HttpApiClient, private val jsonMapper: ObjectMapper) : IRestaurantApi {

    override fun getRestaurantInfo(id: Int): Any {
        TODO("Not yet implemented")
    }

    override fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): CompletableFuture<List<RestaurantResponse>> =
            requestDto(search(latitude, longitude, radiusMeters), RestaurantSearchResultDto::class.java)
                .thenApply(RestaurantSearchResultDto::unDto)

    override fun restaurantDailyMeals(restaurantId: Int): CompletableFuture<List<String>> =
            requestDto(dailyMenu(restaurantId), DailyMenuDto::class.java)
                    .thenApply(DailyMenuDto::unDto)

    private fun <R, D : IUnDto<R>, C : Class<D>> requestDto(urlStr: String, clazz: C): CompletableFuture<D> {
        return clientHttp.request(
                urlStr,
                mapOf(Pair("user-key", ZOMATO_API_KEY), Pair("Accept", "application/json")),
                { false },
                { jsonMapper.readValue(it.body(), clazz) }
        )
    }

    override fun getType() = RestaurantApiType.Zomato
}
