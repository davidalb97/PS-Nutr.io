package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbPortionDto
import pt.isel.ps.g06.httpserver.model.Portion

@Component
class PortionResponseMapper : ResponseMapper<DbPortionDto, Portion> {
    override fun mapTo(dto: DbPortionDto): Portion {
        return Portion(
                amount = dto.quantity,
                unit = Portion.TODO_DEFAULT_UNIT
        )
    }
}