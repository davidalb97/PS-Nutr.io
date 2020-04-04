package pt.isel.ps.g06.httpserver

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import javax.sql.DataSource

@SpringBootApplication
class HttpServerApplication {

}

fun main(args: Array<String>) {
    runApplication<HttpServerApplication>(*args)
}
