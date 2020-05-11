package pt.isel.ps.g06.httpserver.dataAccess.db.dto

class MealCuisineDto(
        meal_submission_id: Int,
        meal_name: String,
        val cuisine_name: String
) : MealDto(meal_submission_id, meal_name)