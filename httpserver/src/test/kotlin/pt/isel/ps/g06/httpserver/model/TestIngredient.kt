package pt.isel.ps.g06.httpserver.model

data class TestIngredient(
        val ingredientName: String,
        val submissionId: Int,
        val apiId: Int,
        val foodApi: TestFoodApi
)