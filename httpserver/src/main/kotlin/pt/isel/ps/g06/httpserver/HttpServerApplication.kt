package pt.isel.ps.g06.httpserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pt.isel.ps.g06.httpserver.dataAccess.api.common.HttpApiClient
import java.net.http.HttpClient

@SpringBootApplication
class HttpServerApplication {
//    @Bean
//    fun zomatoApi(httpApiClient: HttpApiClient): ZomatoRestaurantApi {
//        val objMapper = ObjectMapper()
//                //Ignore unknown json fields
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                //Enable primary constructor on DTOs
//                .registerModule(KotlinModule())
//
//        return ZomatoRestaurantApi(httpApiClient, objMapper)
//    }

//    @Bean
//    fun spoonacularApi(httpApiClient: HttpApiClient): SpoonacularFoodApi {
//        val objMapper = ObjectMapper()
//                //Ignore unknown json fields
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                //Enable primary constructor on DTOs
//                .registerModule(KotlinModule())
//
//        return SpoonacularFoodApi(httpApiClient, objMapper)
//    }

    @Bean
    fun buildApiRequester(): HttpApiClient = HttpApiClient(HttpClient.newHttpClient())
}

fun main(args: Array<String>) {
    runApplication<HttpServerApplication>(*args)
}
