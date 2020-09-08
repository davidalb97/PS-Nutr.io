package pt.isel.ps.g06.httpserver.springConfig

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.springConfig.dto.DatabasePropertiesDto
import javax.sql.DataSource
import javax.validation.Valid


@Configuration
class DatabaseConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    fun setupDatabaseProperties(): DatabasePropertiesDto = DatabasePropertiesDto()

    @Bean
    fun driverManagerDataSource(@Valid properties: DatabasePropertiesDto): DataSource {
        return DataSourceBuilder
                .create()
                .username(properties.username)
                .password(properties.password)
                .driverClassName(properties.`driver-class-name`)
                .url(properties.url)
                .build()
    }

    @Bean
    fun jdbi(dataSource: DataSource?): Jdbi = Jdbi.create(dataSource).installPlugins()
}