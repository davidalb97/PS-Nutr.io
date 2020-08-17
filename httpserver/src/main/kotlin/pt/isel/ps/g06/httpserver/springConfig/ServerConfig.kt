package pt.isel.ps.g06.httpserver.springConfig

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.common.exception.SecretEnvVariableNotSetupException
import pt.isel.ps.g06.httpserver.springConfig.dto.ServerConfigDto
import javax.validation.Valid

@Configuration
class ServerConfig {
    @Bean
    @ConfigurationProperties(prefix = "host.server")
    fun setupServerSecret(): ServerConfigDto = ServerConfigDto()

    @Bean
    fun handleServerSecret(@Valid serverConfigDto: ServerConfigDto): ServerSecret {
        if (serverConfigDto.secret.isNullOrBlank() || serverConfigDto.secret == "\${PS_SERVER_SECRET}") {
            throw SecretEnvVariableNotSetupException()
        }
        return ServerSecret(serverConfigDto.secret!!)
    }
}