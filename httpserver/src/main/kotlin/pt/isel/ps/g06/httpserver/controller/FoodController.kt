package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.IngredientInfoDto

@RestController
@RequestMapping("/test")
class FoodController(val foodApiRepo: FoodApiRepository) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFoodHandler(skip: Int?, count: Int?): IngredientInfoDto? {
        return foodApiRepo
                .getFoodApi(FoodApiType.Spoonacular)
                .ingredientInformation(9266,1, "grams")
                .get()
    }
}

data class test(
    val socorro: String
)