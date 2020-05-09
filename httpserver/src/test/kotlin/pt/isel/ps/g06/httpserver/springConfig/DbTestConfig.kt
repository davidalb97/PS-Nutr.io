package pt.isel.ps.g06.httpserver.springConfig

import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.db.Constants
import pt.isel.ps.g06.httpserver.db.RepoAsserts

@Configuration
class DbTestConfig {

    @Bean
    fun insertConstants(jdbi: Jdbi): Constants
            = Constants(jdbi)

    @Bean
    fun repoAsserts(insertConstants: Constants): RepoAsserts
            = RepoAsserts(insertConstants)
}