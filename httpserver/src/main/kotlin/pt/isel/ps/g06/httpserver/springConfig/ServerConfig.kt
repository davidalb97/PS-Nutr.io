package pt.isel.ps.g06.httpserver.springConfig

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.common.exception.server.SecretEnvVariableNotSetupException
import pt.isel.ps.g06.httpserver.springConfig.dto.InsulinProfilesConfigDto
import pt.isel.ps.g06.httpserver.springConfig.dto.ServerConfigDto
import javax.validation.Valid

@Configuration
class ServerConfig {
    @Bean
    @ConfigurationProperties(prefix = "host.server")
    fun setupServerSecret(): ServerConfigDto = ServerConfigDto()

    @Bean
    @ConfigurationProperties(prefix = "host.insulinprofiles")
    fun setupInsulinProfilesSecret(): InsulinProfilesConfigDto = InsulinProfilesConfigDto()

    @Bean
    fun handleServerSecret(@Valid serverConfigDto: ServerConfigDto): ServerSecret {
        if (serverConfigDto.secret.isNullOrBlank() || serverConfigDto.secret == "\${PS_SERVER_SECRET}") {
            throw SecretEnvVariableNotSetupException()
        }
        return ServerSecret(serverConfigDto.secret!!)
    }

    @Bean
    fun handleInsulinProfilesServerSecret(
            @Valid insulinProfilesConfigDto: InsulinProfilesConfigDto
    ): InsulinProfilesSecret {
        if (insulinProfilesConfigDto.secret.isNullOrBlank() || insulinProfilesConfigDto.secret == "\${PS_INSULIN_PROFILES_SECRET}") {
            throw SecretEnvVariableNotSetupException()
        }
        return InsulinProfilesSecret(insulinProfilesConfigDto.secret!!)
    }
}