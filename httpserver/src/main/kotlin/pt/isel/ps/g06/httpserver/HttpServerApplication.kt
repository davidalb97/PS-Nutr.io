package pt.isel.ps.g06.httpserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@EnableWebMvc
@SpringBootApplication
class HttpServerApplication

fun main(args: Array<String>) {
    runApplication<HttpServerApplication>(*args)
}
