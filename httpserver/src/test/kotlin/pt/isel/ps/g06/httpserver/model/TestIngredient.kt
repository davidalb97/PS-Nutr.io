package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient

data class TestIngredient(
        val name: String,
        val submissionId: Int,
        val apiId: String,
        val foodApi: TestFoodApi
) {

    fun toModel(): Ingredient {
        return Ingredient(
                name,
                apiId,
                foodApi.apiType
        )
    }
}