package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbMealInfoDto
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Votes

@Component
class MealResponseMapper : ResponseMapper<DbMealInfoDto, Meal> {
    override fun mapTo(dto: DbMealInfoDto): Meal {
        return Meal(
                identifier = dto.submission_id,
                name = dto.meal_name,
                votes = Votes(
                        positive = dto.positiveVotes,
                        negative = dto.negativeVotes
                )
        )
    }
}