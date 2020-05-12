package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.CuisineDto

data class TestCuisine(
        val cuisineId: Int,
        val cuisineName: String,
        val api: TestFoodApi,
        val apiSubmissions: List<ApiSubmissionDto>
) {
    fun toCuisineDto() = CuisineDto(
            cuisineId,
            cuisineName
    )
}