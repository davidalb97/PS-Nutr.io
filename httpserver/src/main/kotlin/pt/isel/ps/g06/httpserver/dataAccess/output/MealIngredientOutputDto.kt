package pt.isel.ps.g06.httpserver.dataAccess.output

data class MealIngredientOutputDto(
        val name: String,
        val carbs: Int,
        val amount: Int
)