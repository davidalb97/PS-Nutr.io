package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto

data class TestCuisine(
        val cuisineId: Int,
        val cuisineName: String,
        val api: TestFoodApi,
        val apiSubmissions: List<DbApiSubmissionDto>
) {
    fun toCuisineDto() = DbCuisineDto(
            cuisineId,
            cuisineName
    )
}