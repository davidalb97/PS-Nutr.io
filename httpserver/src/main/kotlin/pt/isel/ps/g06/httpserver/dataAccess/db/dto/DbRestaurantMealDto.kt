package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class DbRestaurantMealDto(
        val submission_id: Int,
        val restaurant_submission_id: Int,
        val meal_submission_id: Int
)