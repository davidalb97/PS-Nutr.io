package pt.isel.ps.g06.httpserver.springConfig

import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.dataAccess.common.TransactionHolder


@Configuration
class ControllerConfiguration {

    @Bean
    fun transactionHolder(jdbi: Jdbi): TransactionHolder = TransactionHolder(jdbi)
}