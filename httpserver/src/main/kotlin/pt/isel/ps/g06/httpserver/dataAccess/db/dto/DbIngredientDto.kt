package pt.isel.ps.g06.httpserver.dataAccess.db.dto

open class DbIngredientDto(
        val submission_id: Int,
        val ingredient_name: String,
        carbs: Int,
        amount: Int,
        unit: String
): DbNutritionalDto(
        carbs = carbs,
        amount = amount,
        unit = unit
)