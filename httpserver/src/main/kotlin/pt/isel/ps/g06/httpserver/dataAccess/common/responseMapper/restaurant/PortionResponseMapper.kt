package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbPortionDto
import pt.isel.ps.g06.httpserver.model.Portion

class DbRestaurantMealPortionsResponseMapper: ResponseMapper<DbPortionDto, Portion> {
    override fun mapTo(dto: DbPortionDto): Portion {
        return Portion(
                amount = dto.quantity,
                //TODO use unit from portion!
                unit = Portion.TODO_DEFAULT_UNIT
        )
    }
}