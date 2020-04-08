package pt.isel.ps.g06.httpserver.dataAccess.model

data class MealResponse(
        val mealName: String,
        val apiId: Int,
        val apiTypeStr: String
)