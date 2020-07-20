package pt.isel.ps.g06.httpserver.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import pt.isel.ps.g06.httpserver.common.CUSTOM_MEAL
import pt.isel.ps.g06.httpserver.common.CUSTOM_MEALS

@Controller
class CustomMealController(

) {
    @GetMapping(CUSTOM_MEALS)
    fun getAllUserCustomMeals() {

    }

    @GetMapping(CUSTOM_MEAL)
    fun getUserCustomMeal() {

    }

    @PostMapping(CUSTOM_MEAL)
    fun createCustomMeal() {

    }

    @DeleteMapping(CUSTOM_MEAL)
    fun deleteCustomMeal() {

    }

}