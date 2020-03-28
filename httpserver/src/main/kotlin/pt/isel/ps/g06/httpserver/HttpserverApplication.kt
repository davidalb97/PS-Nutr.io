package pt.isel.ps.g06.httpserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HttpserverApplication

fun main(args: Array<String>) {
	runApplication<HttpserverApplication>(*args)
}
