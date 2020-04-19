package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.RestaurantDtoMapper

interface IRestaurantApi {

    fun getRestaurantInfo(id: Int): Any

    fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): List<RestaurantDtoMapper>

    fun restaurantDailyMeals(restaurantId: Int): List<String>
}