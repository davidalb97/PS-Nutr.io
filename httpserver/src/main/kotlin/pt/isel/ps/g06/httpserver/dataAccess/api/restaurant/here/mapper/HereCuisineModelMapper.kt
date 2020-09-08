package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbCuisineModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.model.Cuisine

@Component
class HereCuisineModelMapper(
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbCuisineMapper: DbCuisineModelMapper,
        private val apiSubmitterMapper: ApiSubmitterMapper
) : ModelMapper<Sequence<String>, Sequence<Cuisine>> {
    override fun mapTo(dto: Sequence<String>): Sequence<Cuisine> {
        val apiSubmitterId = apiSubmitterMapper.getSubmitter(RestaurantApiType.Here)!!

        return dbCuisineRepo
                .getByApiIds(apiSubmitterId, dto)
                .map(dbCuisineMapper::mapTo)
    }
}