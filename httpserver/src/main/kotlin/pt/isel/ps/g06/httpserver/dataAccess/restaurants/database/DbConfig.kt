package pt.isel.ps.g06.httpserver.dataAccess.restaurants.database

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource


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
}