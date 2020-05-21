package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class DbRestaurantMealPortionDto(
        val meal_submission_id: Int,
        val portion_submission_id: Int,
        val restaurant_submission_id: Int
)