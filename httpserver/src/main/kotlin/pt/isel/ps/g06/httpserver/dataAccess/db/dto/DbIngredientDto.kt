package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class DbIngredientDto(
        val submission_id: Int,
        val ingredient_name: String,
        val carbs: Int,
        val quantity: Int
)