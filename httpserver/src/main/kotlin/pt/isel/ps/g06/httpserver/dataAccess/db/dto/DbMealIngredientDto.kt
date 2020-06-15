package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class DbMealIngredientDto(
        val meal_submission_id: Int,
        val ingredient_submission_id: Int,
        val quantity: Int
)