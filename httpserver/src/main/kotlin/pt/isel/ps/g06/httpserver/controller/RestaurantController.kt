package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/restaurant")
class RestaurantController() {


    @GetMapping("/", produces = ["application/json"])
    fun restaurantsHandler() = ""
}