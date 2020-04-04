package pt.isel.ps.g06.httpserver

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

@SpringBootApplication
class HttpServerApplication {
    @Bean
    fun createJdbi(): Jdbi {
        val dataSource = DataSourceBuilder.create()
                .type()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/Nutrio_db")
                .build()

        return Jdbi.create(dataSource).installPlugins()
    }
}

fun main(args: Array<String>) {
    runApplication<HttpServerApplication>(*args)
}
