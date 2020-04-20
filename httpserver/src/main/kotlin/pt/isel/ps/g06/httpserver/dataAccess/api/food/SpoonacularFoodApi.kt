package pt.isel.ps.g06.httpserver.dataAccess.api.food

import com.fasterxml.jackson.databind.ObjectMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.HttpApiClient
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.*
import java.util.concurrent.CompletableFuture


class SpoonacularFoodApi(private val clientHttp: HttpApiClient, private val jsonMapper: ObjectMapper) : IFoodApi {

    private val uri = SpoonacularUriBuilder()

    override fun productsSearch(
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
            number: Int?
    ): CompletableFuture<ProductSearchContainerDto> {
        return requestDto(
                uri.productsSearchUri(
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
                ),
                ProductSearchContainerDto::class.java
        )
    }

    override fun productSearchAutocompleteUri(
            query: String,
            number: Int?
    ): CompletableFuture<List<ProductSearchAutoComplDto>> {
        return requestDto(
                uri.productSearchAutocompleteUri(query, number),
                ProductSearchAutoComplContainerDto::class.java
        ).thenApply(ProductSearchAutoComplContainerDto::unDto)
    }

    //-------------------------------Recipes------------------------------------

    override fun recipeIngredients(recipeId: String): CompletableFuture<RecipeIngredientsDto> {
        return requestDto(uri.recipeIngredientsUri(recipeId), RecipeIngredientsDto::class.java)
    }

    fun searchRecipes(
            recipeName: String,
            cuisines: Array<SpoonacularCuisine>? = null,
            diet: SpoonacularDiet? = null,
            excludeIngredients: Array<String>? = null,
            intolerances: Array<String>? = null,
            offset: Int? = null,
            number: Int? = null,
            limitLicense: Boolean? = null,
            instructionsRequired: Boolean? = null
    ): CompletableFuture<List<RecipeDto>> {

        return requestDto(
                uri.recipesSearchUri(
                        recipeName,
                        cuisines,
                        diet,
                        excludeIngredients,
                        intolerances,
                        offset,
                        number,
                        limitLicense,
                        instructionsRequired
                ), RecipeContainerDto::class.java
        ).thenApply(RecipeContainerDto::unDto)
    }

    //----------------------------Ingredients----------------------------------

    override fun ingredientSearchAutocomplete(
            query: String,
            number: Int?,
            metaInformation: Boolean?,
            intolerances: Array<String>?
    ): CompletableFuture<IngredientSearchDto> {
        return requestDto(uri.ingredientSearchAutocompleteUri(
                query,
                number,
                metaInformation,
                intolerances
        ), IngredientSearchDto::class.java)
    }

    override fun ingredientInformation(
            id: Int,
            amount: Int?,
            unit: String?
    ): CompletableFuture<IngredientInfoDto> {
        val requestDto = requestDto(uri.ingredientInfoUri(id, amount, unit), IngredientInfoDto::class.java).get()
        return CompletableFuture.completedFuture(requestDto)
    }

    private fun <D> requestDto(urlStr: String, klass: Class<D>): CompletableFuture<D> {
        return clientHttp.request(
                urlStr,
                mapOf(Pair("Accept", "application/json")),
                { false },
                {
                    val a = jsonMapper.readValue(it.body(), klass)
                    return@request a
                }
        )
    }

    override fun getType() = FoodApiType.Spoonacular
}
