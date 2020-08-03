package pt.isel.ps.g06.httpserver.springConfig

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.springConfig.dto.ServerConfigDto

@Configuration
class ServerConfig {
    @Bean
    @ConfigurationProperties(prefix = "host.server")
    fun editableConfig(): ServerConfigDto = ServerConfigDto()
}