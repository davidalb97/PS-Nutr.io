package pt.isel.ps.g06.httpserver.dataAccess.restaurants.api

import pt.isel.ps.g06.httpserver.dataAccess.restaurants.api.dtos.DailyMenuDto
import pt.isel.ps.g06.httpserver.dataAccess.restaurants.api.dtos.RestaurantSearchResultDto
import java.util.concurrent.CompletableFuture

interface IRestaurantApiRepository {

    fun getRestaurantInfo(id: Int): Any

    fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): CompletableFuture<RestaurantSearchResultDto>

    fun restaurantDailyMeals(restaurantId: Int): CompletableFuture<DailyMenuDto>
}