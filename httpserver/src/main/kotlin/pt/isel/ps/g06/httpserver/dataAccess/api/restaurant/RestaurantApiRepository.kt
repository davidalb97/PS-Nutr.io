package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import org.springframework.beans.factory.BeanFactory
import org.springframework.context.annotation.Configuration

@Configuration
class RestaurantApiRepository(factory: BeanFactory) {

    private val restaurantApis = mapOf(
            Pair(RestaurantApiType.Zomato, factory.getBean(ZomatoRestaurantApi::class.java))
    )

    /**
     * @throws NoSuchElementException when api name is invalid
     */
    fun getRestaurantApi(typeName: String): IRestaurantApi =
            getRestaurantApi(RestaurantApiType.values().first { it.name == typeName })


    /**
     * @throws NoSuchElementException when api name is invalid
     */
    fun getRestaurantApi(type: RestaurantApiType): IRestaurantApi = restaurantApis.getValue(type)
}
