package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine

class DbCuisineResponseMapper : ResponseMapper<DbCuisineDto, Cuisine> {
    override fun mapTo(dto: DbCuisineDto): Cuisine {
        return Cuisine(
                identifier = dto.submission_id,
                name = dto.cuisine_name
        )
    }
}

class ZomatoCuisineResponseMapper(
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbCuisineMapper: DbCuisineResponseMapper
) : ResponseMapper<Sequence<String>, Sequence<Cuisine>> {
    override fun mapTo(dto: Sequence<String>): Sequence<Cuisine> {
        return dbCuisineRepo.getAllByNames(dto)
                .map(dbCuisineMapper::mapTo)
    }
}

class HereCuisineResponseMapper(
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbCuisineMapper: DbCuisineResponseMapper
) : ResponseMapper<Sequence<String>, Sequence<Cuisine>> {
    override fun mapTo(dto: Sequence<String>): Sequence<Cuisine> {
        return dbCuisineRepo.getByApiIds(FoodApiType.Here, dto)
                .map(dbCuisineMapper::mapTo)
    }
}