package pt.isel.ps.g06.httpserver.dataAccess.db.dto

open class DbIngredientDto(
        val submission_id: Int,
        val ingredient_name: String,
        carbs: Int,
        quantity: Int,
        //TODO read grams from db table!
        unit: String = "grams"
): DbNutritionalDto(
        carbs = carbs,
        amount = quantity,
        unit = unit
)