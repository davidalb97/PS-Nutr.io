package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto


class DbMealDto(
        val submission_id: String,
        val meal_name: String
) : MealDto(submission_id, meal_name)