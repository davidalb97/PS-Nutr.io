package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class DbPortionDto(
        val submission_id: Int,
        val restaurant_meal_submission_id: Int,
        val quantity: Int
)