package pt.isel.ps.g06.httpserver.dataAccess.common.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.dto.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.mapper.HereRestaurantModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbRestaurantModelMapper
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.util.log

@Component
class RestaurantModelMapper(
        private val hereRestaurantModelMapper: HereRestaurantModelMapper,
        private val dbRestaurantModelMapper: DbRestaurantModelMapper
) : ModelMapper<RestaurantDto, Restaurant> {

    override fun mapTo(dto: RestaurantDto): Restaurant {
        return when (dto) {
            is HereResultItem -> hereRestaurantModelMapper.mapTo(dto)
            is DbRestaurantDto -> dbRestaurantModelMapper.mapTo(dto)
            else -> {
                log("ERROR: Unregistered mapper for RestaurantDto of type '${dto.javaClass.typeName}'!")
                throw NoSuchMethodException("There is no mapper registered for given dto type!")
            }
        }
    }
}