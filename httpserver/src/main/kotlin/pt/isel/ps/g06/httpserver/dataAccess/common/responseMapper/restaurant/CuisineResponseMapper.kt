package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.model.Cuisine

@Component
class DbCuisineResponseMapper : ResponseMapper<DbCuisineDto, Cuisine> {
    override fun mapTo(dto: DbCuisineDto): Cuisine {
        return Cuisine(
                identifier = dto.submission_id,
                name = dto.cuisine_name
        )
    }
}

@Component
class ZomatoCuisineResponseMapper(
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbCuisineMapper: DbCuisineResponseMapper
) : ResponseMapper<Sequence<String>, Sequence<Cuisine>> {
    override fun mapTo(dto: Sequence<String>): Sequence<Cuisine> {
        return dbCuisineRepo.getAllByNames(dto)
                .map(dbCuisineMapper::mapTo)
    }
}

@Component
class HereCuisineResponseMapper(
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbCuisineMapper: DbCuisineResponseMapper,
        private val apiSubmitterMapper: ApiSubmitterMapper
) : ResponseMapper<Sequence<String>, Sequence<Cuisine>> {
    override fun mapTo(dto: Sequence<String>): Sequence<Cuisine> {
        val apiSubmitterId = apiSubmitterMapper.getSubmitter(RestaurantApiType.Here)!!

        return dbCuisineRepo
                .getByApiIds(apiSubmitterId, dto)
                .map(dbCuisineMapper::mapTo)
    }
}