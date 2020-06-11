package pt.isel.ps.g06.httpserver.model

class MealIngredient(
        val submissionId: Int,
        val name: String,
        val carbs: Int,
        val amount: Int,
        val image: String?
)