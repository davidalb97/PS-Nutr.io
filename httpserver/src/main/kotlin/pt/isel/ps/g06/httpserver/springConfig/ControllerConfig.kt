package pt.isel.ps.g06.httpserver.springConfig

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import pt.isel.ps.g06.httpserver.dataAccess.common.TransactionHolder
import javax.sql.DataSource


@Configuration
class ControllerConfiguration {

    @Bean
    fun transactionHolder(jdbi: Jdbi): TransactionHolder = TransactionHolder(jdbi)
}