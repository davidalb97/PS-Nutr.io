package pt.isel.ps.g06.httpserver.model

import java.time.OffsetDateTime

data class TestMeal(
        val mealName: String,
        val submitterId: Int,
        val submissionId: Int,
        val foodApiId: String?,
        val foodApi: TestFoodApi,
        val date: OffsetDateTime,
        val ingredients: Collection<TestIngredient> = emptyList(),
        val cuisines: Collection<TestCuisine> = emptyList(),
        val restaurantMeals: Collection<TestRestaurantMeal> = emptyList()
)