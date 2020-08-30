package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.model.NutritionalValues
import pt.isel.ps.g06.httpserver.model.food.Ingredient

@Component
class IngredientResponseMapper : ResponseMapper<DbMealDto, Ingredient> {
    override fun mapTo(dto: DbMealDto): Ingredient {
        return Ingredient(
                identifier = dto.submission_id,
                name = dto.meal_name,
                nutritionalValues = NutritionalValues(
                        dto.carbs,
                        dto.amount,
                        dto.unit
                )
        )
    }
}