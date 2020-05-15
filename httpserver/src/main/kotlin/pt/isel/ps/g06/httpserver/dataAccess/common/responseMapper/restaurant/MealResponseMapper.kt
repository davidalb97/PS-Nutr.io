package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.model.Meal

@Component
class MealResponseMapper : ResponseMapper<DbMealDto, Meal> {
    override fun mapTo(dto: DbMealDto): Meal {
        return Meal(identifier = dto.submission_id, name = dto.meal_name)
    }
}