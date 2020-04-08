package pt.isel.ps.g06.httpserver.dataAccess.db.concrete


data class DbMeal(
        val meal_id: Int,
        val meal_name: String,
        val cuisine_name: String
)