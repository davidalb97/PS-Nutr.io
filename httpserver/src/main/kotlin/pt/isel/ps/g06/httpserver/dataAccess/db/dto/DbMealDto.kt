package pt.isel.ps.g06.httpserver.dataAccess.db.dto

open class DbMealDto(
        val submission_id: Int,
        val meal_name: String,
        val meal_type: String,
        carbs: Float,
        quantity: Float,
        unit: String
) : DbNutritionalDto(
        carbs = carbs,
        amount = quantity,
        unit = unit
)