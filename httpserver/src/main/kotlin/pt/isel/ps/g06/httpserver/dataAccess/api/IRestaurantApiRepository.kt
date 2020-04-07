package pt.isel.ps.g06.httpserver.dataAccess.api

import pt.isel.ps.g06.httpserver.dataAccess.dto.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.dto.RestaurantSearchResultDto
import java.util.concurrent.CompletableFuture

interface IRestaurantApiRepository {

    fun getRestaurantInfo(id: Int): Any

    fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): CompletableFuture<RestaurantSearchResultDto>

    fun restaurantDailyMeals(restaurantId: Int): CompletableFuture<DailyMenuDto>
}