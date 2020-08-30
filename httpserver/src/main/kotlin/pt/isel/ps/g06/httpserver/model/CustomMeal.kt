package pt.isel.ps.g06.httpserver.model

class CustomMeal(
        val submission_id: Int,
        val meal_name: String,
        val meal_portion: Int,
        val carb_amount: Int,
        val image_url: String
)