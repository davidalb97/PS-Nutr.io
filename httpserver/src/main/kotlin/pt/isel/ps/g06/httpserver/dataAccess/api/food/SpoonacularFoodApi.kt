package pt.isel.ps.g06.httpserver.dataAccess.api.food

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.uri.FoodUri
import java.net.http.HttpClient

@Repository
class SpoonacularFoodApi(
        httpClient: HttpClient,
        uriBuilder: FoodUri,
        responseMapper: ObjectMapper
) : FoodApi(httpClient, uriBuilder, responseMapper) {
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
