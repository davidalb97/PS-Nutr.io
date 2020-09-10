package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.cuisines

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import pt.isel.ps.g06.httpserver.model.Cuisine

@Component
class CuisinesResponseMapper : ResponseMapper<DbCuisineDto, Cuisine> {
    override fun mapTo(dto: DbCuisineDto): Cuisine {
        return Cuisine(dto.submission_id, dto.cuisine_name)
    }
}