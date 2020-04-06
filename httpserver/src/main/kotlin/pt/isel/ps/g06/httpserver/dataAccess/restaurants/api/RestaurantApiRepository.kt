package pt.isel.ps.g06.httpserver.dataAccess.restaurants.api

import org.springframework.beans.factory.BeanFactory
import org.springframework.context.annotation.Configuration


@Configuration
class ApiRepository(factory: BeanFactory) {
    val registeredRestaurantApis = mapOf(
            Pair(RestaurantApiType.ZOMATO, factory.getBean(ZomatoRestaurantApiRepository::class.java))
    )

    fun getRestaurantApi(type: RestaurantApiType): IRestaurantApiRepository = registeredRestaurantApis[type]
            ?: throw NoSuchElementException("No such RestaurantApiRepository for given Api Type! Was it registered in the mapper?")
}

enum class RestaurantApiType { ZOMATO }