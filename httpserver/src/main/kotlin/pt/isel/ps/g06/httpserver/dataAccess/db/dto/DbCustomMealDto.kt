package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class DbCustomMealDto(
        val submission_id: Int,
        val meal_name: String,
        val meal_portion: Int,
        val carb_amount: Int,
        val image_url: String
)