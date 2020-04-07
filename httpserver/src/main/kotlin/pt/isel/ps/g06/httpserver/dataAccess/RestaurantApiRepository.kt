package pt.isel.ps.g06.httpserver.dataAccess

import org.springframework.beans.factory.BeanFactory
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.dataAccess.api.IRestaurantApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.api.ZomatoRestaurantApiRepository

@Configuration
class RestaurantApiRepository(factory: BeanFactory) {
    private val registeredRestaurantApis = mapOf(
            Pair(RestaurantApiType.ZOMATO, factory.getBean(ZomatoRestaurantApiRepository::class.java))
    )

    fun getRestaurantApi(type: RestaurantApiType): IRestaurantApiRepository {
        return registeredRestaurantApis[type]
                ?: throw NoSuchElementException("No such RestaurantApiRepository for given Api Type! Was it registered in the mapper?")
    }
}
