package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.cuisines.CuisinesResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.model.Cuisine
import java.util.stream.Stream

@Service
class CuisinesService(
        private val cuisineDbRepository: CuisineDbRepository,
        private val cuisinesMapper: CuisinesResponseMapper
) {
    fun getAvailableCuisines(skip: Int?, count: Int?): Stream<Cuisine> {
        return cuisineDbRepository
                .getAll(skip, count)
                .stream()       //TODO Remove 'to stream' call
                .map(cuisinesMapper::mapTo)
    }
}