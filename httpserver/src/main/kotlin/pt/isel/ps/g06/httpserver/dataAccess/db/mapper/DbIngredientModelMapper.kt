package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.MealIngredient
import pt.isel.ps.g06.httpserver.model.NutritionalValues
import pt.isel.ps.g06.httpserver.model.modular.toUserPredicate

//TODO Replace this with first mapper
@Component
class DbIngredientModelMapper : ModelMapper<DbMealDto, MealIngredient> {
    override fun mapTo(dto: DbMealDto): MealIngredient {
        return MealIngredient(
                identifier = dto.submission_id,
                name = dto.meal_name,
                image = null,
                nutritionalInfo = NutritionalValues(dto.carbs, dto.amount, dto.unit)
        )
    }
}