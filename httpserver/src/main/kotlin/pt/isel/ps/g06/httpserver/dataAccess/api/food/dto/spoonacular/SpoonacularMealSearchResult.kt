package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.spoonacular

import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto

data class SpoonacularMealSearchResult(val results: Collection<SpoonacularMealDto>)

class SpoonacularMealDto(
        id: Int,
        title: String
) : MealDto(id, title)