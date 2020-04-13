package pt.isel.ps.g06.httpserver.dataAccess.api.food

import pt.isel.ps.g06.httpserver.dataAccess.api.AUriBuilder

private const val SPOONACULAR_BASE_URL = "https://api.spoonacular.com/"
private const val SPOONACULAR_SEARCH_URL = "${SPOONACULAR_BASE_URL}/recipes/search?"
private const val SPOONACULAR_INGREDIENT_INFO_URL = "${SPOONACULAR_BASE_URL}/food/ingredients/"
private const val SPOONACULAR_GROCERY_PRODUCTS_URL = "${SPOONACULAR_BASE_URL}/food/products/search?"
private const val SPOONACULAR_INGREDIENT_SEARCH_AUTO_COMPL_URL = "${SPOONACULAR_BASE_URL}/food/ingredients/autocomplete?"
private const val SPOONACULAR_PRODUCT_SEARCH_AUTO_COMPL_URL = "${SPOONACULAR_BASE_URL}/food/products/suggest?"
private const val SPOONACULAR_RECIPE_INGREDIENTS_URL = "${SPOONACULAR_BASE_URL}/recipes/"

class SpoonacularUriBuilder : AUriBuilder() {

    //-------------------------------Products------------------------------------

    /**
     * https://spoonacular.com/food-api/docs#Search-Grocery-Products
     */
    fun productsSearchUri(
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
    ): String {
        return "${SPOONACULAR_GROCERY_PRODUCTS_URL}query=$query" +
                param("minCalories", minCalories) +
                param("maxCalories", maxCalories) +
                param("minCarbs", minCarbs) +
                param("maxCarbs", maxCarbs) +
                param("minProtein", minProtein) +
                param("maxProtein", maxProtein) +
                param("minFat", minFat) +
                param("maxFat", maxFat) +
                param("offset", offset) +
                param("number", number)
    }

    /**
     * https://spoonacular.com/food-api/docs#Autocomplete-Product-Search
     */
    fun productSearchAutocompleteUri(query: String, number: Int? = null): String =
            "${SPOONACULAR_PRODUCT_SEARCH_AUTO_COMPL_URL}query=$query" +
                    param("number", number)

//-------------------------------Recipes------------------------------------

    /**
     * https://spoonacular.com/food-api/docs#Get-Recipe-Ingredients-by-ID
     */
    fun recipeIngredientsUri(recipeId: String): String =
            "${SPOONACULAR_RECIPE_INGREDIENTS_URL}$recipeId/ingredientWidget.json"

    /**
     * https://spoonacular.com/food-api/docs#Search-Recipes
     */
    fun recipesSearchUri(
            recipeName: String,
            cuisines: Array<SpoonacularCuisine>? = null,
            diet: SpoonacularDiet? = null,
            excludeIngredients: Array<String>? = null,
            intolerances: Array<String>? = null,
            offset: Int? = null,
            number: Int? = null,
            limitLicense: Boolean? = null,
            instructionsRequired: Boolean? = null
    ): String {
        return "${SPOONACULAR_SEARCH_URL}query=$recipeName" +
                param("cuisine", cuisines) +
                param("diet", diet) +
                param("offset", offset) +
                param("number", number) +
                param("limitLicense", limitLicense) +
                param("instructionsRequired", instructionsRequired) +
                param("excludeIngredients", excludeIngredients) +
                param("intolerances", intolerances)
    }

//----------------------------Ingredients----------------------------------

    /**
     * https://spoonacular.com/food-api/docs#Autocomplete-Ingredient-Search
     */
    fun ingredientSearchAutocompleteUri(
            query: String,
            number: Int? = null,
            metaInformation: Boolean? = null,
            intolerances: Array<String>? = null
    ): String {
        return "${SPOONACULAR_INGREDIENT_SEARCH_AUTO_COMPL_URL}query=$query" +
                param("number", number) +
                param("metaInformation", metaInformation) +
                param("intolerances", intolerances)
    }

    /**
     * https://spoonacular.com/food-api/docs#Get-Ingredient-Information
     */
    fun ingredientInfoUri(id: Int, amount: Int?, unit: String?): String {
        val hasParams = amount != null || unit != null
        return "$SPOONACULAR_INGREDIENT_INFO_URL/$id/information" +
                (if (hasParams) "?" else "") +
                param("amount", amount) +
                param("unit", unit)
    }
}

