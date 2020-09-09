package pt.isel.ps.g06.httpserver.springConfig

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.common.MAIN_FILE_PATH
import pt.isel.ps.g06.httpserver.common.INDEX_FILE_PATH
import pt.isel.ps.g06.httpserver.springConfig.dto.WebClientFilesDto

@Configuration
class WebClientConfig {

    @Bean
    fun webClientFiles(): WebClientFilesDto {
        return WebClientFilesDto(
                indexHtml = requireNotNull(getFile(INDEX_FILE_PATH)) {
                    "Missing file $INDEX_FILE_PATH"
                },
                mainJs = requireNotNull(getFile(MAIN_FILE_PATH)) {
                    "Missing file $MAIN_FILE_PATH"
                }
        )
    }

    /**
     * Gets a resource file.
     */
    private fun getFile(path: String): ByteArray? {
        return javaClass.classLoader.getResourceAsStream(path)?.use {
            it.readBytes()
        }
    }
}