package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/meal")
class MealController() {

    @GetMapping("/{mealId}", produces = ["application/json"])
    fun getMealInformation(@PathVariable mealId: String) = ""
}