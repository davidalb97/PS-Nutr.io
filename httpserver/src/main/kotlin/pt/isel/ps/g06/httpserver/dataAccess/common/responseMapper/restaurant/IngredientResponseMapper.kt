package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbMealIngredientInfoDto
import pt.isel.ps.g06.httpserver.model.MealIngredient

@Component
class IngredientResponseMapper : ResponseMapper<DbMealIngredientInfoDto, MealIngredient> {

    override fun mapTo(dto: DbMealIngredientInfoDto): MealIngredient {
        return MealIngredient(
                submissionId = dto.ingredient.submissionId,
                name = dto.ingredient.name,
                carbs = dto.carbs,
                amount = dto.amount,
                image = dto.ingredient.image,
                isFavorite = dto.ingredient.isFavorite
        )
    }
}