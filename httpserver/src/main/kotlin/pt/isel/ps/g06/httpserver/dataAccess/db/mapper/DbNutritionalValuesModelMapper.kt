package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbNutritionalDto
import pt.isel.ps.g06.httpserver.model.NutritionalValues

@Component
class DbNutritionalValuesModelMapper : ModelMapper<DbNutritionalDto, NutritionalValues> {
    override fun mapTo(dto: DbNutritionalDto): NutritionalValues {
        return NutritionalValues(
                carbs = dto.carbs,
                amount = dto.amount,
                unit = dto.unit
        )
    }
}