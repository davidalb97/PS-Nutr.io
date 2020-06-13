package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class DbMealIngredientDto(
        val meal_submission_id: Int,
        val ingredient_submission_id: Int,
        carbs: Int,
        quantity: Int,
        //TODO Read DbMealIngredientDto.unit from db table!
        unit: String = "grams"
): DbNutritionalDto(
        carbs = carbs,
        amount = quantity,
        unit = unit
)