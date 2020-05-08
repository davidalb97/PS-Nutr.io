package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.IRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.ZomatoRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType

@Component
class RestaurantApiMapper(private val zomatoApi: ZomatoRestaurantApi) {

    private val restaurantApis = mapOf(
            Pair(RestaurantApiType.Zomato, zomatoApi)
    )

    /**
     *  Get a restaurantApi from given [RestaurantApiType]
     *  Defaults to [ZomatoRestaurantApi]
     */
    fun getRestaurantApi(type: RestaurantApiType): IRestaurantApi = restaurantApis.getOrDefault(type, zomatoApi)
}
