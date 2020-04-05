package pt.isel.ps.g06.httpserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class HttpServerApplication

fun main(args: Array<String>) {
    runApplication<HttpServerApplication>(*args)
}
