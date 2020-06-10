package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class DbMealDto(
        val submission_id: Int,
        val meal_name: String,
        val carbs: Int,
        val quantity: Int
)