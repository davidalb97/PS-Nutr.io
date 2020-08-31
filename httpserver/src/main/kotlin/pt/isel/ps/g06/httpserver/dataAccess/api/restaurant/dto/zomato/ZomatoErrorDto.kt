package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.zomato

data class ZomatoErrorDto(
        val code: Int,
        val status: String,
        val message: String
)