package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.source.dto.RestaurantDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant

class RestaurantMapper :
    IResponseMapper<RestaurantDto, Restaurant> {
    override fun map(dto: RestaurantDto): Restaurant =
        Restaurant(
            dto.id,
            dto.name,
            dto.latitude,
            dto.longitude,
            dto.votes,
            dto.cuisines,
            dto.meals
        )
}

class RestaurantsMapper(
    private val restaurantMapper: RestaurantMapper
) {

    fun map(dtos: Array<RestaurantDto>): List<Restaurant> =
        dtos.map { restaurantMapper.map(it) }
}
