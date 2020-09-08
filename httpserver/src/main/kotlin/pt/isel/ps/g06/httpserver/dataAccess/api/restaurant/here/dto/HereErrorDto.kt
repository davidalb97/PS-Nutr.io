package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.dto

data class HereErrorDto(
        val status: Int,
        val title: String,
        val code: String?,
        val cause: String?
)