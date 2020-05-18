package pt.isel.ps.g06.httpserver.dataAccess.api.food

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.common.BaseApiRequester
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.uri.FoodUri
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Repository
abstract class FoodApi(
        httpClient: HttpClient = HttpClient.newHttpClient(),
        private val foodUri: FoodUri,
        responseMapper: ObjectMapper
) : BaseApiRequester(httpClient, responseMapper) {
    //TODO - Uncomment as needed. I dislike the "adding as much and never needing it" policy.

    fun searchMeals(name: String, cuisines: Collection<String>?): CompletableFuture<Collection<MealDto>> {
        val uri = foodUri.searchMeals(name, cuisines)
        val request = buildGetRequest(uri)

        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::handleSearchMealResponse)
    }

    protected abstract fun handleSearchMealResponse(response: HttpResponse<String>): Collection<MealDto>

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