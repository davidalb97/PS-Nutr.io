package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbPortionDto
import pt.isel.ps.g06.httpserver.model.Portion

@Component
class DbRestaurantMealPortionResponseMapper : ResponseMapper<DbPortionDto, Portion> {
    override fun mapTo(dto: DbPortionDto): Portion {
        return Portion(
                identifier = dto.submission_id,
                amount = dto.quantity,
                unit = "gr"
        )
    }
}