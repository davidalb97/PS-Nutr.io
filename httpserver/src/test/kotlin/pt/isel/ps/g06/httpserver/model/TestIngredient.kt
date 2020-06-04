package pt.isel.ps.g06.httpserver.model

import java.time.OffsetDateTime

data class TestIngredient(
        val name: String,
        val submissionId: Int,
        val date: OffsetDateTime,
        val apiId: String,
        val foodApi: TestFoodApi
) {

    fun toModel(): Ingredient {
        return Ingredient(
                name,
                apiId
        )
    }
}