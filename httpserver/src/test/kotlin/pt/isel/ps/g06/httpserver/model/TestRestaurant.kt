package pt.isel.ps.g06.httpserver.model

import java.time.OffsetDateTime

data class TestRestaurant(
        val name: String,
        val submitterId: Int,
        val submissionId: Int,
        val restaurantApiId: String?,
        val restaurantApi: TestRestaurantApi,
        val date: OffsetDateTime,
        val ingredients: Collection<TestIngredient> = emptyList(),
        val cuisines: Collection<TestCuisine> = emptyList(),
        val restaurantMeals: Collection<TestRestaurantMeal> = emptyList()
)