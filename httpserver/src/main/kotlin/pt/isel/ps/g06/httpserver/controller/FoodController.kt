package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularCuisine
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularDiet
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularUnitTypes
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.IngredientInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.ProductSearchContainerDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.RecipeDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.RecipeIngredientsDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.mapper.FoodApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.food.model.FoodApiType

@RestController
@RequestMapping("/spoonacular")
class FoodController(foodApiRepo: FoodApiMapper) {
    val spoonacularFoodApi = foodApiRepo.getFoodApi(FoodApiType.Spoonacular)

    @GetMapping("/product", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun productSearch(
            @RequestParam query: String,
            @RequestParam minCalories: Int?,
            @RequestParam maxCalories: Int?,
            @RequestParam minCarbs: Int?,
            @RequestParam maxCarbs: Int?,
            @RequestParam minProtein: Int?,
            @RequestParam maxProtein: Int?,
            @RequestParam minFat: Int?,
            @RequestParam maxFat: Int?,
            @RequestParam offset: Int?,
            @RequestParam number: Int?,
            @RequestParam skip: Int?,
            @RequestParam count: Int?
    ): ProductSearchContainerDto? {
        return spoonacularFoodApi.productsSearch(
                query,
                minCalories,
                maxCalories,
                minCarbs,
                maxCarbs,
                minProtein,
                maxProtein,
                minFat,
                maxFat,
                offset,
                number
        )
    }

    @GetMapping("/recipeIngredient", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun recipeIngredient(@RequestParam recipeId: String): RecipeIngredientsDto {
        return spoonacularFoodApi.recipeIngredients(recipeId)
    }

    @GetMapping("/recipes", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchRecipes(@RequestParam recipeName: String,
                      @RequestParam cuisines: Array<SpoonacularCuisine>,
                      @RequestParam diet: SpoonacularDiet?,
                      @RequestParam excludeIngredients: Array<String>?,
                      @RequestParam intolerances: Array<String>?,
                      @RequestParam offset: Int?,
                      @RequestParam number: Int?,
                      @RequestParam limitLicense: Boolean?,
                      @RequestParam instructionsRequired: Boolean?,
                      @RequestParam skip: Int?,
                      @RequestParam count: Int?
    ): List<RecipeDto> {
        return spoonacularFoodApi.searchRecipes(
                recipeName,
                cuisines,
                diet,
                excludeIngredients,
                intolerances,
                offset,
                number,
                limitLicense,
                instructionsRequired
        )
    }

    /*fun ingredientSearchAutocomplete(
            @RequestParam query: String,
            @RequestParam number: Int?,
            @RequestParam metaInformation: Boolean?,
            @RequestParam intolerances: Array<String>?
    ): CompletableFuture<IngredientSearchDto> {
        return spoonacularFoodApi
                .ingredientSearchAutocomplete(
                        query,
                        number,
                        metaInformation,
                        intolerances
                )
    }*/

    @GetMapping("/ingredient", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getIngredientInfo(
            @RequestParam id: Int,
            @RequestParam amount: Int,
            @RequestParam unit: SpoonacularUnitTypes,
            @RequestParam skip: Int?,
            @RequestParam count: Int?
    ): IngredientInfoDto? {
        return spoonacularFoodApi.ingredientInformation(id, amount, unit)
    }
}