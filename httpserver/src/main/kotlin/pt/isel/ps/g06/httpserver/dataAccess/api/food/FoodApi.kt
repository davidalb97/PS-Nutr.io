package pt.isel.ps.g06.httpserver.dataAccess.api.food

import com.fasterxml.jackson.databind.ObjectMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.common.BaseApiRequester
import pt.isel.ps.g06.httpserver.dataAccess.api.food.uri.FoodUri
import java.net.http.HttpClient

abstract class FoodApi(
        httpClient: HttpClient = HttpClient.newHttpClient(),
        protected val uriBuilder: FoodUri,
        responseMapper: ObjectMapper
) : BaseApiRequester(httpClient, responseMapper) {
    //TODO - Uncomment as needed. I dislike the "adding as much and never needing it" policy.
//
//    fun productsSearch(
//            query: String,
//            minCalories: Int? = null,
//            maxCalories: Int? = null,
//            minCarbs: Int? = null,
//            maxCarbs: Int? = null,
//            minProtein: Int? = null,
//            maxProtein: Int? = null,
//            minFat: Int? = null,
//            maxFat: Int? = null,
//            offset: Int? = null,
//            number: Int? = null
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
//
//    }
//
//    fun productSearchAutocompleteUri(
//            query: String,
//            number: Int? = null
//    ): List<ProductSearchAutoComplDto>
//
//    fun recipeIngredients(recipeId: String): RecipeIngredientsDto
//
//    fun searchRecipes(
//            recipeName: String,
//            cuisines: Array<SpoonacularCuisine>? = null,
//            diet: SpoonacularDiet? = null,
//            excludeIngredients: Array<String>? = null,
//            intolerances: Array<String>? = null,
//            offset: Int? = null,
//            number: Int? = null,
//            limitLicense: Boolean? = null,
//            instructionsRequired: Boolean? = null
//    ): List<RecipeDto>
//
//    fun ingredientSearchAutocomplete(
//            query: String,
//            number: Int? = null,
//            metaInformation: Boolean? = null,
//            intolerances: Array<String>? = null
//    ): IngredientSearchDto
//
//    fun ingredientInformation(
//            id: Int,
//            amount: Int? = null,
//            unit: SpoonacularUnitTypes
//    ): IngredientInfoDto

}