package pt.isel.ps.g06.httpserver.dataAccess.restaurants.api

import com.fasterxml.jackson.databind.ObjectMapper
import pt.isel.ps.g06.httpserver.dataAccess.HttpApiClient
import pt.isel.ps.g06.httpserver.dataAccess.restaurants.api.dtos.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.restaurants.api.dtos.RestaurantSearchResultDto
import java.util.concurrent.CompletableFuture

private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"

class ZomatoRestaurantApiRepository(private val clientHttp: HttpApiClient, private val jsonMapper: ObjectMapper): IRestaurantApiRepository {

    override fun getRestaurantInfo(id: Int): Any {
        TODO("Not yet implemented")
    }

    override fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): CompletableFuture<RestaurantSearchResultDto> {
        //TODO - Check errors
        return clientHttp.request(
                search(latitude, longitude, radiusMeters),
                mapOf(Pair("user-key", ZOMATO_API_KEY), Pair("Accept", "application/json")),
                { false },
                { jsonMapper.readValue(it.body(), RestaurantSearchResultDto::class.java) }
        )
    }

    override fun restaurantDailyMeals(restaurantId: Int): CompletableFuture<DailyMenuDto> {
        return clientHttp.request(
                dailyMenu(restaurantId),
                mapOf(Pair("user-key", ZOMATO_API_KEY), Pair("Accept", "application/json")),
                { false },
                { jsonMapper.readValue(it.body(), DailyMenuDto::class.java) }
        )
    }
}
