package pt.isel.ps.g06.httpserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pt.isel.ps.g06.httpserver.argumentResolver.UserAuthenticationArgumentResolver
import pt.isel.ps.g06.httpserver.interceptor.LoggerInterceptor

@EnableWebMvc
@SpringBootApplication
class HttpServerApplication

@Configuration
class InterceptorConfig(
        private val loggerInterceptor: LoggerInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggerInterceptor)
    }
}

@Configuration
class ArgumentResolverConfig(
        private val userAuthenticationArgumentResolver: UserAuthenticationArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userAuthenticationArgumentResolver)
    }
}

fun main(args: Array<String>) {
    runApplication<HttpServerApplication>(*args)
}
