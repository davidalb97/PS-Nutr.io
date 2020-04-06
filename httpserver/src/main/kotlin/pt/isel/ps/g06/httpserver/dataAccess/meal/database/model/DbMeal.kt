package pt.isel.ps.g06.httpserver.dataAccess.meal.database.model


data class DbMeal(
        val meal_id: Int,
        val meal_name: String,
        val cuisine_name: String
)