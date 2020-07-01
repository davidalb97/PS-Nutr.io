package pt.isel.ps.g06.httpserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pt.isel.ps.g06.httpserver.common.interceptor.AuthenticationInterceptor

@EnableWebMvc
@SpringBootApplication
class HttpServerApplication

@Configuration
class InterceptorConfig(private val authenticationInterceptor: AuthenticationInterceptor) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
    }
}

fun main(args: Array<String>) {
    runApplication<HttpServerApplication>(*args)
}
