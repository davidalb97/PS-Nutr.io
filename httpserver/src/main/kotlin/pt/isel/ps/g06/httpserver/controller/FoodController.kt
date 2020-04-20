package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType

@RestController
@RequestMapping("/test")
class FoodController(val foodApiRepo: FoodApiRepository) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFoodHandler(skip: Int?, count: Int?) {
        foodApiRepo
                .getFoodApi(FoodApiType.Spoonacular.name)
                .ingredientInformation(1,1, "grams")
    }
}

data class test(
    val socorro: String
)