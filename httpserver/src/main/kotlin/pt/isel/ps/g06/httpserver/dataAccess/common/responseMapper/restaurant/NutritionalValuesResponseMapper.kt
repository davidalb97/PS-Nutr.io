package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbNutritionalDto
import pt.isel.ps.g06.httpserver.model.NutritionalValues

class DbNutritionalValuesResponseMapper: ResponseMapper<DbNutritionalDto, NutritionalValues> {
    override fun mapTo(dto: DbNutritionalDto): NutritionalValues {
        return NutritionalValues(
                carbs = dto.carbs,
                amount = dto.amount,
                unit = dto.unit
        )
    }
}