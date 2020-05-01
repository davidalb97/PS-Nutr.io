package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.dataAccess.api.food.*
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/spoonacular")
class FoodController(private final val foodApiRepo: FoodApiRepository) {

    val spoonacularFoodApi = foodApiRepo.getFoodApi(FoodApiType.Spoonacular)

    @GetMapping("/product", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun productSearch(
            query: String,
            minCalories: Int?,
            maxCalories: Int?,
            minCarbs: Int?,
            maxCarbs: Int?,
            minProtein: Int?,
            maxProtein: Int?,
            minFat: Int?,
            maxFat: Int?,
            offset: Int?,
            number: Int?,
            skip: Int?,
            count: Int?
    ): ProductSearchContainerDto? {
        return spoonacularFoodApi
                .productsSearch(
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
                ).get()
    }

    @GetMapping("/recipeIngredient", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun recipeIngredient(recipeId: String): RecipeIngredientsDto {
        return spoonacularFoodApi.recipeIngredients(recipeId).get()
    }

    @GetMapping("/recipes", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchRecipes(recipeName: String,
                      cuisines: Array<SpoonacularCuisine>,
                      diet: SpoonacularDiet?,
                      excludeIngredients: Array<String>?,
                      intolerances: Array<String>?,
                      offset: Int?,
                      number: Int?,
                      limitLicense: Boolean?,
                      instructionsRequired: Boolean?,
                      skip: Int?,
                      count: Int?
    ): List<RecipeDto> {
        return spoonacularFoodApi
                .searchRecipes(
                        recipeName,
                        cuisines,
                        diet,
                        excludeIngredients,
                        intolerances,
                        offset,
                        number,
                        limitLicense,
                        instructionsRequired
                ).get()
    }

    /*fun ingredientSearchAutocomplete(
            query: String,
            number: Int?,
            metaInformation: Boolean?,
            intolerances: Array<String>?
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
            id: Int,
            amount: Int,
            unit: SpoonacularUnitTypes,
            skip: Int?,
            count: Int?
    ): IngredientInfoDto? {
        return spoonacularFoodApi
                .ingredientInformation(id, amount, unit)
                .get()
    }
}