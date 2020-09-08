package pt.isel.ps.g06.httpserver.springConfig

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.common.exception.server.InvalidSecretEnvVariableException
import pt.isel.ps.g06.httpserver.common.exception.server.SecretEnvVariableNotSetupException
import pt.isel.ps.g06.httpserver.springConfig.bean.InsulinProfilesSecret
import pt.isel.ps.g06.httpserver.springConfig.dto.InsulinProfilesConfigDto
import java.security.InvalidKeyException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.validation.Valid

private const val ALGORITHM_TRANSFORMATION = "AES/ECB/PKCS5Padding"
private const val ALGORITHM_AES = "AES"

@Configuration
class InsulinProfilesSecretConfig {

    @Bean
    @ConfigurationProperties(prefix = "host.insulinprofiles")
    fun setupInsulinProfilesSecret(): InsulinProfilesConfigDto = InsulinProfilesConfigDto()

    @Bean
    fun handleInsulinProfilesServerSecret(
            @Valid insulinProfilesConfigDto: InsulinProfilesConfigDto
    ): InsulinProfilesSecret {
        if (insulinProfilesConfigDto.secret.isNullOrBlank() || insulinProfilesConfigDto.secret == "\${PS_INSULIN_PROFILES_SECRET}") {
            throw SecretEnvVariableNotSetupException()
        }

        val key = SecretKeySpec(insulinProfilesConfigDto.secret!!.toByteArray(), ALGORITHM_AES)
        val secret = InsulinProfilesSecret(
                key = key,
                transformation = ALGORITHM_TRANSFORMATION
        )

        try {
            //Force Cipher initialization to test error
            Cipher.getInstance(secret.transformation).init(Cipher.ENCRYPT_MODE, secret.key)
        } catch (e: InvalidKeyException) {
            throw InvalidSecretEnvVariableException(e)
        }
        return secret
    }
}