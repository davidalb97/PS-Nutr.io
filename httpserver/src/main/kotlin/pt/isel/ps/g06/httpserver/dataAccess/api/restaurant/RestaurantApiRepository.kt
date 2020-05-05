package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import org.springframework.beans.factory.BeanFactory
import org.springframework.context.annotation.Configuration

private val DEFAULT_API = RestaurantApiType.Zomato

@Configuration
class RestaurantApiRepository(factory: BeanFactory) {
    private final val zomatoApi = factory.getBean(ZomatoRestaurantApi::class.java)

    private val restaurantApis = mapOf(
            Pair(RestaurantApiType.Zomato, zomatoApi)
    )

    /**
     *  Get a restaurantApi from given apiType.
     *  If no string matches an apiType, returns the default one.
     */
    fun getRestaurantApi(typeName: String?): IRestaurantApi {
        return if (typeName == null) zomatoApi else {
            val type = RestaurantApiType.getType(typeName) ?: DEFAULT_API
            restaurantApis.getOrDefault(type, zomatoApi)
        }
    }
}
