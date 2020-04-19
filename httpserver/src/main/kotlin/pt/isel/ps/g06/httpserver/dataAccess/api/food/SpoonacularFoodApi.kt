package pt.isel.ps.g06.httpserver.dataAccess.api.food

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.HttpApiClient
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.*
import java.util.concurrent.CompletableFuture

private const val SPOONACULCAR_API_KEY = "9d90d89e9ecc4844a88385816df04fec"

class SpoonacularFoodApi(private val clientHttp: HttpApiClient, private val jsonMapper: ObjectMapper) : IFoodApi {

    private val uri = SpoonacularUriBuilder()

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

    fun productSearchAutocompleteUri(
            query: String,
            number: Int? = null
    ): CompletableFuture<List<ProductSearchAutoComplDto>> {
        return requestDto<ProductSearchAutoComplContainerDtoMapper>(
                uri.productSearchAutocompleteUri(query, number)
        ).thenApply(ProductSearchAutoComplContainerDtoMapper::mapDto)
    }

    //-------------------------------Recipes------------------------------------

    fun recipeIngredients(recipeId: String): CompletableFuture<RecipeIngredientsDto> {
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

        return requestDto<RecipeContainerDtoMapper>(
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
        ).thenApply(RecipeContainerDtoMapper::mapDto)
    }

    //----------------------------Ingredients----------------------------------

    fun ingredientSearchAutocomplete(
            query: String,
            number: Int? = null,
            metaInformation: Boolean? = null,
            intolerances: Array<String>? = null
    ): CompletableFuture<List<IngredientSearchDto>> {
        return requestDto(uri.ingredientSearchAutocompleteUri(
                query,
                number,
                metaInformation,
                intolerances
        ))
    }

    fun ingredientInformation(
            id: Int,
            amount: Int? = null,
            unit: String? = null
    ): CompletableFuture<IngredientInfoDto> {
        return requestDto(uri.ingredientInfoUri(id, amount, unit))
    }

    private fun <D> requestDto(urlStr: String): CompletableFuture<D> {
        return CompletableFuture.supplyAsync {
            clientHttp.request(
                    urlStr,
                    mapOf(
                            Pair("user-key", SPOONACULCAR_API_KEY),
                            Pair("Accept", "application/json")
                    ),
                    { false },
                    { jsonMapper.readValue(it.body(), object : TypeReference<D>() {}) }
            )
        }
    }

    override fun getType() = FoodApiType.Spoonacular
}
