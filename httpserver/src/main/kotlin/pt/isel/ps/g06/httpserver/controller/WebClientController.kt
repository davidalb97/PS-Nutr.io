package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.INDEX_FILE_PATH
import pt.isel.ps.g06.httpserver.common.MAIN_FILE_PATH
import pt.isel.ps.g06.httpserver.common.hypermedia.APPLICATION_JAVASCRIPT_VALUE
import pt.isel.ps.g06.httpserver.service.WebClientService

@Validated
@Suppress("MVCPathVariableInspection")
@RestController
@RequestMapping
class WebClientController(private val webClientService: WebClientService) {

    /**
     * Gets release index.html from [INDEX_FILE_PATH] resource.
     */
    @GetMapping(path = ["/**"], produces = [MediaType.TEXT_HTML_VALUE])
    fun getIndex(): ResponseEntity<Any> {
        return ResponseEntity.ok(webClientService.getIndex())
    }

    /**
     * Gets release main.js from [MAIN_FILE_PATH] resource dir.
     */
    @GetMapping(
            value = ["/$MAIN_FILE_PATH"],
            produces = [APPLICATION_JAVASCRIPT_VALUE]
    )
    fun getClientApp(): ResponseEntity<Any> {
        return ResponseEntity.ok(webClientService.getMain())
    }
}