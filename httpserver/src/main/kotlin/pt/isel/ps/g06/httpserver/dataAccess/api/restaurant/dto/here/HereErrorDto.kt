package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here

data class HereErrorDto(
        val status: Int,
        val title: String,
        val code: String,
        val cause: String
)