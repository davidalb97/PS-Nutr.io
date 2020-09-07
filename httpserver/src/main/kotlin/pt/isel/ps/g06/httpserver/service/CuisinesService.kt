package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.mapper.cuisines.CuisinesResponseMapper
import pt.isel.ps.g06.httpserver.model.Cuisine

@Service
class CuisinesService(
        private val cuisineDbRepository: CuisineDbRepository,
        private val cuisinesMapper: CuisinesResponseMapper
) {
    fun getAvailableCuisines(skip: Int?, count: Int?): Sequence<Cuisine> {
        return cuisineDbRepository
                .getAll(skip, count)
                .map(cuisinesMapper::mapTo)
    }
}