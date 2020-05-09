package pt.isel.ps.g06.httpserver.model

data class TestMeal(
        val mealName: String,
        val submitterId: Int,
        val submissionId: Int,
        val foodApiId: String?,
        val foodApi: TestFoodApi,
        val ingredients: List<TestIngredient> = emptyList(),
        val cuisines: List<String> = emptyList()
)