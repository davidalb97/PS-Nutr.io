package pt.isel.ps.g06.httpserver.dataAccess.api.food.uri

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularCuisine
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularDiet
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularUnitTypes
import java.net.URI

@Component
interface FoodUri {
    fun searchMeals(name: String, cuisines: Collection<String>?): URI

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
    ): URI

    fun productSearchAutocomplete(query: String, number: Int? = null): URI

    fun recipeIngredients(recipeId: String): URI

    fun recipesSearch(
            recipeName: String,
            cuisines: Array<SpoonacularCuisine>? = null,
            diet: SpoonacularDiet? = null,
            excludeIngredients: Array<String>? = null,
            intolerances: Array<String>? = null,
            offset: Int? = null,
            number: Int? = null,
            limitLicense: Boolean? = null,
            instructionsRequired: Boolean? = null
    ): URI

    fun ingredientSearchAutocomplete(
            query: String,
            number: Int? = null,
            metaInformation: Boolean? = null,
            intolerances: Array<String>? = null
    ): URI

    fun ingredientInfo(id: Int, amount: Int?, unit: SpoonacularUnitTypes): URI
}