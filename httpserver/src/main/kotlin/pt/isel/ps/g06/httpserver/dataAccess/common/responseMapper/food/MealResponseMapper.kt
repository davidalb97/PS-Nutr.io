package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.model.Meal


@Component
class MealResponseMapper : ResponseMapper<DbMealDto, Meal> {
    override fun mapTo(dto: DbMealDto): Meal {
        //TODO
        return Meal(
                dto.submission_id,
                dto.meal_name,
                emptySequence(),
                10
        )
    }
}