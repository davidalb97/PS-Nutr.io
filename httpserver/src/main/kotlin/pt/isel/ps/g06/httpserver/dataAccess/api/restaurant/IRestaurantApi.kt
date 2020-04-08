package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.RestaurantSearchResultDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantResponse
import java.util.concurrent.CompletableFuture

interface IRestaurantApi {

    fun getRestaurantInfo(id: Int): Any

    fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): CompletableFuture<List<RestaurantResponse>>

    fun restaurantDailyMeals(restaurantId: Int): CompletableFuture<List<String>>

    fun getType(): RestaurantApiType
}