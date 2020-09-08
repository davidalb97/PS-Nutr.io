package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbCuisineModelMapper
import pt.isel.ps.g06.httpserver.model.Cuisine

@Service
class CuisinesService(
        private val cuisineDbRepository: CuisineDbRepository,
        private val dbCuisineModelMapper: DbCuisineModelMapper
) {
    fun getAvailableCuisines(skip: Int?, count: Int?): Sequence<Cuisine> {
        return cuisineDbRepository
                .getAll(skip, count)
                .map(dbCuisineModelMapper::mapTo)
    }
}