package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ingredients")
class IngredientsController() {

    @GetMapping(produces = ["application/json"])
    fun getIngredientsHandler(skip: Int?, count: Int?) = ""
}