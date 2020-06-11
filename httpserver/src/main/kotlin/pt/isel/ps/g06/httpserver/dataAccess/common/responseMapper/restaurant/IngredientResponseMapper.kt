package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbMealIngredientInfoDto
import pt.isel.ps.g06.httpserver.model.MealIngredient

class IngredientResponseMapper : ResponseMapper<DbMealIngredientInfoDto, MealIngredient> {

    override fun mapTo(dto: DbMealIngredientInfoDto): MealIngredient {
        return MealIngredient(
                submissionId = dto.ingredient.submission_id,
                name = dto.ingredient.ingredient_name,
                carbs = dto.carbs,
                amount = dto.amount,
                image = dto.image
        )
    }
}