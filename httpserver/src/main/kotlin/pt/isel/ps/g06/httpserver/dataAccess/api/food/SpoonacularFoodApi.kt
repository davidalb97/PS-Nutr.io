package pt.isel.ps.g06.httpserver.dataAccess.api.food

import com.fasterxml.jackson.core.type.TypeReference
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
                )
        )
    }

    override fun productSearchAutocompleteUri(
            query: String,
            number: Int?
    ): CompletableFuture<List<ProductSearchAutoComplDto>> {
        return requestDto<ProductSearchAutoComplContainerDto>(
                uri.productSearchAutocompleteUri(query, number)
        ).thenApply(ProductSearchAutoComplContainerDto::unDto)
    }

    //-------------------------------Recipes------------------------------------

    override fun recipeIngredients(recipeId: String): CompletableFuture<RecipeIngredientsDto> {
        return requestDto(uri.recipeIngredientsUri(recipeId))
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

        return requestDto<RecipeContainerDto>(
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
                )
        ).thenApply(RecipeContainerDto::unDto)
    }

    //----------------------------Ingredients----------------------------------

    override fun ingredientSearchAutocomplete(
            query: String,
            number: Int?,
            metaInformation: Boolean?,
            intolerances: Array<String>?
    ): CompletableFuture<List<IngredientSearchDto>> {
        return requestDto(uri.ingredientSearchAutocompleteUri(
                query,
                number,
                metaInformation,
                intolerances
        ))
    }

    override fun ingredientInformation(
            id: Int,
            amount: Int?,
            unit: String?
    ): CompletableFuture<IngredientInfoDto> {
        return requestDto(uri.ingredientInfoUri(id, amount, unit))
    }

    private fun <D> requestDto(urlStr: String): CompletableFuture<D> {
        return clientHttp.request(
                urlStr,
                mapOf(Pair("Accept", "application/json")),
                { false },
                { jsonMapper.readValue(it.body(), object : TypeReference<D>() {}) }
        )
    }

    override fun getType() = FoodApiType.Spoonacular
}
