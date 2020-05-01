package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import org.springframework.context.annotation.Configuration

private val DEFAULT_API = RestaurantApiType.Zomato

@Configuration
class RestaurantApiMapper(
        private val zomatoApi: ZomatoRestaurantApi
) {

    private val restaurantApis = mapOf(
            Pair(RestaurantApiType.Zomato, zomatoApi)
    )

    /**
     *  Get a restaurantApi from given [RestaurantApiType]
     *  Defaults to [ZomatoRestaurantApi]
     */
    fun getRestaurantApi(type: RestaurantApiType): IRestaurantApi = restaurantApis.getOrDefault(type, zomatoApi)
}
