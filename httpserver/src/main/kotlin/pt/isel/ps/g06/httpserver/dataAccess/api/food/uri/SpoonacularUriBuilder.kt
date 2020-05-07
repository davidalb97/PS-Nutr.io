package pt.isel.ps.g06.httpserver.dataAccess.api.food.uri

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.dataAccess.api.common.nonNullQueryParam
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularCuisine
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularDiet
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularUnitTypes
import java.net.URI

private const val SPOONACULAR_BASE_URL = "https://api.spoonacular.com/"
private const val SPOONACULAR_SEARCH_URL = "${SPOONACULAR_BASE_URL}recipes/search"
private const val SPOONACULAR_INGREDIENT_INFO_URL = "${SPOONACULAR_BASE_URL}food/ingredients/"
private const val SPOONACULAR_GROCERY_PRODUCTS_URL = "${SPOONACULAR_BASE_URL}food/products/search"
private const val SPOONACULAR_INGREDIENT_SEARCH_AUTO_COMPL_URL = "${SPOONACULAR_BASE_URL}food/ingredients/autocomplete"
private const val SPOONACULAR_PRODUCT_SEARCH_AUTO_COMPL_URL = "${SPOONACULAR_BASE_URL}food/products/suggest"
private const val SPOONACULAR_RECIPE_INGREDIENTS_URL = "${SPOONACULAR_BASE_URL}recipes/"

private const val SPOONACULAR_API_KEY = "9d90d89e9ecc4844a88385816df04fec"

//Set of query params
private const val API_KEY = "apiKey"
private const val QUERY = "query"
private const val MIN_CAL = "minCalories"
private const val MAX_CAL = "maxCalories"
private const val MIN_CARB = "minCarbs"
private const val MAX_CARB = "maxCarbs"
private const val MIN_PROTEIN = "minProtein"
private const val MAX_PROTEIN = "maxProtein"
private const val MIN_FAT = "minFat"
private const val MAX_FAT = "maxFat"
private const val OFFSET = "offset"
private const val NUMBER = "number"
private const val CUISINE = "cuisine"
private const val DIET = "diet"
private const val LIMIT_LICENSE = "limitLicense"
private const val INSTRUCTIONS_REQUIRED = "instructionsRequired"
private const val EXCLUDE_INGREDIENTS = "excludeIngredients"
private const val INTOLERANCES = "intolerances"
private const val META_INFO = "metaInformation"
private const val AMOUNT = "amount"
private const val UNIT = "unit"

@Component
class SpoonacularUriBuilder {
    //-------------------------------Products------------------------------------

    /**
     * From Spoonacular [documentation.](https://spoonacular.com/food-api/docs#Search-Grocery-Products)
     */
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
    ): URI {
        return baseUri(SPOONACULAR_GROCERY_PRODUCTS_URL)
                .queryParam(QUERY, query)
                .nonNullQueryParam(MIN_CAL, minCalories)
                .nonNullQueryParam(MAX_CAL, maxCalories)
                .nonNullQueryParam(MIN_CARB, minCarbs)
                .nonNullQueryParam(MAX_CARB, maxCarbs)
                .nonNullQueryParam(MIN_PROTEIN, minProtein)
                .nonNullQueryParam(MAX_PROTEIN, maxProtein)
                .nonNullQueryParam(MIN_FAT, minFat)
                .nonNullQueryParam(MAX_FAT, maxFat)
                .nonNullQueryParam(OFFSET, offset)
                .nonNullQueryParam(NUMBER, number)
                .build()
                .toUri()
    }

    /**
     * From Spoonacular [documentation.](https://spoonacular.com/food-api/docs#Autocomplete-Product-Search)
     */
    fun productSearchAutocomplete(query: String, number: Int? = null): URI {
        return baseUri(SPOONACULAR_PRODUCT_SEARCH_AUTO_COMPL_URL)
                .queryParam(QUERY, query)
                .nonNullQueryParam(NUMBER, number)
                .build()
                .toUri()
    }

//-------------------------------Recipes------------------------------------

    /**
     * From Spoonacular [documentation.](https://spoonacular.com/food-api/docs#Get-Recipe-Ingredients-by-ID)
     */
    fun recipeIngredients(recipeId: String): URI {
        return baseUri("$SPOONACULAR_RECIPE_INGREDIENTS_URL$recipeId/ingredientWidget.json")
                .build()
                .toUri()
    }

    /**
     * From Spoonacular [documentation.](https://spoonacular.com/food-api/docs#Search-Recipes)
     */
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
    ): URI {
        return baseUri(SPOONACULAR_SEARCH_URL)
                .nonNullQueryParam(QUERY, recipeName)
                .nonNullQueryParam(CUISINE, cuisines)
                .nonNullQueryParam(DIET, diet)
                .nonNullQueryParam(OFFSET, offset)
                .nonNullQueryParam(NUMBER, number)
                .nonNullQueryParam(LIMIT_LICENSE, limitLicense)
                .nonNullQueryParam(INSTRUCTIONS_REQUIRED, instructionsRequired)
                .nonNullQueryParam(EXCLUDE_INGREDIENTS, excludeIngredients)
                .nonNullQueryParam(INTOLERANCES, intolerances)
                .build()
                .toUri()
    }

//----------------------------Ingredients----------------------------------

    /**
     * From Spoonacular [documentation.](https://spoonacular.com/food-api/docs#Autocomplete-Ingredient-Search)
     */
    fun ingredientSearchAutocomplete(
            query: String,
            number: Int? = null,
            metaInformation: Boolean? = null,
            intolerances: Array<String>? = null
    ): URI {
        return baseUri(SPOONACULAR_INGREDIENT_SEARCH_AUTO_COMPL_URL)
                .queryParam(QUERY, query)
                .nonNullQueryParam(NUMBER, number)
                .nonNullQueryParam(META_INFO, metaInformation)
                .nonNullQueryParam(INTOLERANCES, intolerances)
                .build()
                .toUri()
    }

    /**
     * From Spoonacular [documentation.]( https://spoonacular.com/food-api/docs#Get-Ingredient-Information)
     */
    fun ingredientInfo(id: Int, amount: Int?, unit: SpoonacularUnitTypes): URI {
        return baseUri("$SPOONACULAR_INGREDIENT_INFO_URL$id/information")
                .nonNullQueryParam(AMOUNT, amount)
                .nonNullQueryParam(UNIT, unit)
                .build()
                .toUri()
    }

    /**
     * Convinience function that builds a basic [UriComponentsBuilder] with [SPOONACULAR_API_KEY] already inserted as
     * a query parameter, since all requests require it.
     */
    private fun baseUri(path: String): UriComponentsBuilder {
        return UriComponentsBuilder.fromPath(path).queryParam(API_KEY, SPOONACULAR_API_KEY)
    }
}
