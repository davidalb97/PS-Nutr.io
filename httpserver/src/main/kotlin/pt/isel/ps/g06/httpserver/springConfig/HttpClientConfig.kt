package pt.isel.ps.g06.httpserver.springConfig

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient

@Configuration
class HttpClientConfig {
    @Bean
    fun configureHttpClient(): HttpClient {
        return HttpClient.newHttpClient()
    }
}