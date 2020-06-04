package pt.isel.ps.g06.httpserver.dataAccess.api.food

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.spoonacular.SpoonacularDetailedMealDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.spoonacular.SpoonacularMealSearchResult
import pt.isel.ps.g06.httpserver.dataAccess.api.food.uri.SpoonacularUriBuilder
import pt.isel.ps.g06.httpserver.util.log
import java.net.http.HttpClient
import java.net.http.HttpResponse

@Repository
class SpoonacularFoodApi(
        httpClient: HttpClient,
        foodUri: SpoonacularUriBuilder,
        responseMapper: ObjectMapper
) : FoodApi(httpClient, foodUri, responseMapper) {
    override fun handleSearchMealResponse(response: HttpResponse<String>): Collection<MealDto> {
        val body = response.body()

        return when (response.statusCode()) {
            HttpStatus.OK.value() -> mapToMealDtoCollection(body)
            HttpStatus.UNAUTHORIZED.value() -> {
                //TODO We need proper Logging
                log("WARN: Spoonacular refused access. Is the API-Key valid?")
                return emptyList()
            }
            else -> emptyList()
        }
    }

    override fun handleMealInfoResponse(response: HttpResponse<String>): MealDto? {
        val body = response.body()

        return when (response.statusCode()) {
            HttpStatus.OK.value() -> mapToDetailedMealDto(body)
            HttpStatus.UNAUTHORIZED.value() -> {
                //TODO We need proper Logging
                log("WARN: Spoonacular refused access. Is the API-Key valid?")
                return null
            }
            else -> null
        }
    }

    private fun mapToMealDtoCollection(body: String): Collection<MealDto> {
        return responseMapper.readValue(body, SpoonacularMealSearchResult::class.java).results
    }

    private fun mapToDetailedMealDto(body: String): SpoonacularDetailedMealDto {
        return responseMapper.readValue(body, SpoonacularDetailedMealDto::class.java)
    }
//
//    override fun productsSearch(
//            query: String,
//            minCalories: Int?,
//            maxCalories: Int?,
//            minCarbs: Int?,
//            maxCarbs: Int?,
//            minProtein: Int?,
//            maxProtein: Int?,
//            minFat: Int?,
//            maxFat: Int?,
//            offset: Int?,
//            number: Int?
//    ): ProductSearchContainerDto {
//        val uri = uriBuilder.productsSearch(
//                query,
//                minCalories,
//                maxCalories,
//                minCarbs,
//                maxCarbs,
//                minProtein,
//                maxProtein,
//                minFat,
//                maxFat,
//                offset,
//                number
//        )
//
//        return requestDto(uri, ProductSearchContainerDto::class.java)
//    }
//
//    override fun productSearchAutocompleteUri(query: String, number: Int?): List<ProductSearchAutoComplDto> {
//        val uri = uriBuilder.productSearchAutocomplete(query, number)
//        return emptyList()
////        return  requestDto(uri, ProductSearchAutoComplContainerDtoMapper::class.java).
////        return requestDto(
////                uriBuilder.productSearchAutocompleteUri(query, number),
////                ProductSearchAutoComplContainerDto::class.java
////        ).thenApply(ProductSearchAutoComplContainerDto::unDto)
//    }
//
//    //-------------------------------Recipes------------------------------------
//
//    override fun recipeIngredients(recipeId: String): RecipeIngredientsDto {
//        return requestDto(uriBuilder.recipeIngredients(recipeId), RecipeIngredientsDto::class.java)
//    }
//
//    override fun searchRecipes(
//            recipeName: String,
//            cuisines: Array<SpoonacularCuisine>?,
//            diet: SpoonacularDiet?,
//            excludeIngredients: Array<String>?,
//            intolerances: Array<String>?,
//            offset: Int?,
//            number: Int?,
//            limitLicense: Boolean?,
//            instructionsRequired: Boolean?
//    ): List<RecipeDto> {
//        return emptyList()
////        return requestDto(
////                uriBuilder.recipesSearchUri(
////                        recipeName,
////                        cuisines,
////                        diet,
////                        excludeIngredients,
////                        intolerances,
////                        offset,
////                        number,
////                        limitLicense,
////                        instructionsRequired
////                ), RecipeContainerDto::class.java
////        ).thenApply(RecipeContainerDto::unDto)
//    }
//
//    //----------------------------Ingredients----------------------------------
//
//    override fun ingredientSearchAutocomplete(
//            query: String,
//            number: Int?,
//            metaInformation: Boolean?,
//            intolerances: Array<String>?
//    ): IngredientSearchDto {
//        val uri = uriBuilder.ingredientSearchAutocomplete(
//                query,
//                number,
//                metaInformation,
//                intolerances
//        )
//        return requestDto(uri, IngredientSearchDto::class.java)
//    }
//
//    override fun ingredientInformation(id: Int, amount: Int?, unit: SpoonacularUnitTypes): IngredientInfoDto {
//        val uri = uriBuilder.ingredientInfo(id, amount, unit)
//        return requestDto(uri, IngredientInfoDto::class.java)
//    }
}
