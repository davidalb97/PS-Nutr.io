package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType

data class TestMeal(
        val mealName: String,
        val submitterId: Int,
        val submissionId: Int,
        val foodApiId: Int?,
        val foodApi: TestFoodApi,
        val ingredients: List<TestIngredient>
)