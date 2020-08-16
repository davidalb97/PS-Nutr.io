package pt.isel.ps.g06.httpserver.springConfig

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource


@Configuration
class DatabaseConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    fun driverManagerDataSource(): DataSource {
        return DriverManagerDataSource()
    }

    @Bean
    fun jdbi(dataSource: DataSource?): Jdbi = Jdbi.create(dataSource).installPlugins()
}