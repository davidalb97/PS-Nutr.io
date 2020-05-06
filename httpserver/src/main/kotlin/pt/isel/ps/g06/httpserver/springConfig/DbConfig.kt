package pt.isel.ps.g06.httpserver.springConfig

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.convert.DurationUnit
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.sql.DataSource
import javax.validation.constraints.Max
import javax.validation.constraints.Min


@Configuration
class DatabaseConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    fun driverManagerDataSource(): DataSource {
        return DriverManagerDataSource()
    }

//    @Bean
//    fun dataSourceTransactionManager(dataSource: DataSource?): DataSourceTransactionManager {
//        val dataSourceTransactionManager = DataSourceTransactionManager()
//        dataSourceTransactionManager.dataSource = dataSource
//        return dataSourceTransactionManager
//    }

    @Bean
    fun jdbi(dataSource: DataSource?): Jdbi = Jdbi.create(dataSource).installPlugins()

    @Bean
    @ConfigurationProperties(prefix = "db")
    fun config(): DbConfig = DbConfig()
}

class DbConfig {
    @Min(0)
    @Max(30)
    @DurationUnit(ChronoUnit.MINUTES)
    var `edit-timeout-minutes`: Duration? = null
}