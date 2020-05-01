package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cuisines")
class CuisinesController {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCuisinesHandler(skip: Int?, count: Int?) {
    }
}