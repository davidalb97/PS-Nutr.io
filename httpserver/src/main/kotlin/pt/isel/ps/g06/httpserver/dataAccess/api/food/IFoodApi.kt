package pt.isel.ps.g06.httpserver.dataAccess.api.food

import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.*
import java.util.concurrent.CompletableFuture

interface IFoodApi {

    fun productsSearch(
            query: String,
            minCalories: Int? = null,
            maxCalories: Int? = null,
            minCarbs: Int? = null,
            maxCarbs: Int? = null,
            minProtein: Int? = null,
            maxProtein: Int? = null,
            minFat: Int? = null,
            maxFat: Int? = null,
            offset: Int? = null,
            number: Int? = null
    ): CompletableFuture<ProductSearchContainerDto>

    fun getType(): FoodApiType

    fun productSearchAutocompleteUri(
            query: String,
            number: Int? = null
    ): CompletableFuture<List<ProductSearchAutoComplDto>>

    fun recipeIngredients(recipeId: String): CompletableFuture<RecipeIngredientsDto>

    fun ingredientSearchAutocomplete(
            query: String,
            number: Int? = null,
            metaInformation: Boolean? = null,
            intolerances: Array<String>? = null
    ): CompletableFuture<List<IngredientSearchDto>>

    fun ingredientInformation(
            id: Int,
            amount: Int? = null,
            unit: String? = null
    ): CompletableFuture<IngredientInfoDto>

}