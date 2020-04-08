package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.RestaurantSearchResultDto
import java.util.concurrent.CompletableFuture

interface IRestaurantApi {

    fun getRestaurantInfo(id: Int): Any

    fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): CompletableFuture<RestaurantSearchResultDto>

    fun restaurantDailyMeals(restaurantId: Int): CompletableFuture<DailyMenuDto>

    fun getType(): RestaurantApiType
}