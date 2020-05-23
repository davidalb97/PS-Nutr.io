package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.spoonacular

import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto

data class SpoonacularMealSearchResult(val results: Collection<SpoonacularMealDto>)

class SpoonacularMealDto(
        id: String,
        title: String
) : MealDto(id, title)

class SpoonacularDetailedMealDto(
        id: String,
        title: String,
        val nutrition: SpoonacularMealNutrition
) : MealDto(id, title)

data class SpoonacularMealNutrition(
        val nutrients: Collection<SpoonacularNutrient>,
        val ingredients: Collection<SpoonacularIngredients>
)

data class SpoonacularNutrient(val title: String, val amount: Float, val unit: String)

data class SpoonacularIngredients(val name: String, val amount: Float, val unit: String)