package pt.isel.ps.g06.httpserver.dataAccess.api.food.uri

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.dataAccess.api.common.nonNullQueryParam
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularCuisine
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularDiet
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularUnitTypes
import java.net.URI

private const val BASE_URI = "https://api.spoonacular.com/"
private const val RECIPES = "${BASE_URI}recipes"
private const val SEARCH_MEALS = "${RECIPES}/search"
private const val MEAL_INFO = "${BASE_URI}recipes/"

private const val SPOONACULAR_INGREDIENT_INFO_URL = "${BASE_URI}food/ingredients/"
private const val SPOONACULAR_GROCERY_PRODUCTS_URL = "${BASE_URI}food/products/search"
private const val SPOONACULAR_INGREDIENT_SEARCH_AUTO_COMPL_URL = "${BASE_URI}food/ingredients/autocomplete"
private const val SPOONACULAR_PRODUCT_SEARCH_AUTO_COMPL_URL = "${BASE_URI}food/products/suggest"
private const val SPOONACULAR_RECIPE_INGREDIENTS_URL = "${BASE_URI}recipes/"

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
private const val INCLUDE_NUTRITION = "includeNutrition"

private const val DEFAULT_AMOUNT = 100

@Component
class SpoonacularUriBuilder : FoodUri {

    /**
     * From Spoonacular [documentation.](https://spoonacular.com/food-api/docs#Search-Recipes)
     */
    override fun searchMeals(name: String, cuisines: Collection<String>?): URI {
        var builder = baseUri(SEARCH_MEALS)
                .queryParam(QUERY, name)
                .queryParam(NUMBER, DEFAULT_AMOUNT)

        if (cuisines != null && cuisines.isNotEmpty()) {
            builder = builder.queryParam(CUISINE, cuisines)
        }
        return builder.build().toUri()
    }

    override fun getMealInfo(mealId: String): URI {
        return baseUri("$RECIPES/$mealId/information")
                .queryParam(INCLUDE_NUTRITION, true)
                .build()
                .toUri()
    }

    //-------------------------------Products------------------------------------
    /**
     * From Spoonacular [documentation.](https://spoonacular.com/food-api/docs#Search-Grocery-Products)
     */
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
    override fun productSearchAutocomplete(query: String, number: Int?): URI {
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
    override fun recipeIngredients(recipeId: String): URI {
        return baseUri("$SPOONACULAR_RECIPE_INGREDIENTS_URL$recipeId/ingredientWidget.json")
                .build()
                .toUri()
    }

    /**
     * From Spoonacular [documentation.](https://spoonacular.com/food-api/docs#Search-Recipes)
     */
    override fun recipesSearch(
            recipeName: String,
            cuisines: Array<SpoonacularCuisine>?,
            diet: SpoonacularDiet?,
            excludeIngredients: Array<String>?,
            intolerances: Array<String>?,
            offset: Int?,
            number: Int?,
            limitLicense: Boolean?,
            instructionsRequired: Boolean?
    ): URI {
        return baseUri(SEARCH_MEALS)
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
    override fun ingredientSearchAutocomplete(
            query: String,
            number: Int?,
            metaInformation: Boolean?,
            intolerances: Array<String>?
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
    override fun ingredientInfo(id: Int, amount: Int?, unit: SpoonacularUnitTypes): URI {
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
        return UriComponentsBuilder.fromHttpUrl(path).queryParam(API_KEY, SPOONACULAR_API_KEY)
    }
}

