package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCustomMealDto
import pt.isel.ps.g06.httpserver.model.CustomMeal

class CustomMealDbMapper {

    fun mapToModel(dto: DbCustomMealDto): CustomMeal =
            CustomMeal(
                    submission_id = dto.submission_id,
                    meal_name = dto.meal_name,
                    meal_portion = dto.meal_portion,
                    carb_amount = dto.carb_amount,
                    image_url = dto.image_url
            )


    fun mapToDto(model: CustomMeal): DbCustomMealDto =
            DbCustomMealDto(
                    submission_id = model.submission_id,
                    meal_name = model.meal_name,
                    meal_portion = model.meal_portion,
                    carb_amount = model.carb_amount,
                    image_url = model.image_url
            )
}