package pt.isel.ps.g06.httpserver.springConfig

import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.db.InsertConstants


@Configuration
class TestDatabaseConfiguration {

    @Bean
    fun insertConstants(jdbi: Jdbi): InsertConstants = InsertConstants(jdbi)
}