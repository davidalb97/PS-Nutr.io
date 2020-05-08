package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.HereRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.IRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.ZomatoRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.model.RestaurantApiType

@Component
class RestaurantApiMapper(private val zomatoApi: ZomatoRestaurantApi, hereApi: HereRestaurantApi) {

    private val restaurantApis = mapOf<RestaurantApiType, IRestaurantApi>(
            Pair(RestaurantApiType.Zomato, zomatoApi),
            Pair(RestaurantApiType.Here, hereApi)
    )

    /**
     *  Get a restaurantApi from given [RestaurantApiType]
     *  Defaults to [ZomatoRestaurantApi]
     */
    fun getRestaurantApi(type: RestaurantApiType): IRestaurantApi = restaurantApis.getOrDefault(type, zomatoApi)
}
