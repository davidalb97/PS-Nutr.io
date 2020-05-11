package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealCuisineDto
import pt.isel.ps.g06.httpserver.model.Meal

@Component
class MealResponseMapper : ResponseMapper<MealCuisineDto, Meal> {
    override fun mapTo(dto: MealCuisineDto): Meal {
        return Meal(identifier = dto.meal_submission_id, name = "Placeholder")
    }
}