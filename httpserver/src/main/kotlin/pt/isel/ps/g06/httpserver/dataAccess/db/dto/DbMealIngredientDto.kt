package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class DbMealIngredientDto(
        val meal_submission_id: Int,
        val ingredient_submission_id: Int,
        carbs: Int,
        amount: Int,
        unit: String
): DbNutritionalDto(
        carbs = carbs,
        amount = amount,
        unit = unit
)