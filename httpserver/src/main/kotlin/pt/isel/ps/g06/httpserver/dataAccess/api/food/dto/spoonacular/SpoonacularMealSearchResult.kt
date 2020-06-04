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
        val nutrition: SpoonacularMealNutrition,
        val extendedIngredients: Collection<SpoonacularIngredient>
) : MealDto(id, title)

data class SpoonacularMealNutrition(val nutrients: Collection<SpoonacularNutrient>)

data class SpoonacularNutrient(val title: String, val amount: Float, val unit: String)

data class SpoonacularIngredient(val id: String, val name: String, val measures: SpoonacularMeasureContainer)

data class SpoonacularMeasureContainer(val us: SpoonacularMeasure, val metric: SpoonacularMeasure)

data class SpoonacularMeasure(val amount: Float, val unitShort: String)