package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.springConfig.dto.WebClientFilesDto

@Service
class WebClientService(private val webClientFilesDto: WebClientFilesDto) {

    fun getIndex(): ByteArray = webClientFilesDto.indexHtml

    fun getMain(): ByteArray = webClientFilesDto.mainJs
}