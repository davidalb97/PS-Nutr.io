package pt.isel.ps.g06.httpserver.dataAccess.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.dataAccess.ApiRequester
import java.net.http.HttpClient

@Configuration
class ZomatoApiConfig {

    @Bean
    fun zomatoApi(apiRequester: ApiRequester): ZomatoApi {
        val objMapper = ObjectMapper()
                //Ignore unknown json fields
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //Enable primary constructor on DTOs
                .registerModule(KotlinModule())

        return ZomatoApi(apiRequester, objMapper)
    }

    @Bean
    fun buildApiRequester(): ApiRequester = ApiRequester(HttpClient.newHttpClient())
}