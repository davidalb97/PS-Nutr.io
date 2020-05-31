package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.ApiRestaurantDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant

class RestaurantMapper :
    IResponseMapper<ApiRestaurantDto, Restaurant> {
    override fun map(dto: ApiRestaurantDto): Restaurant =
        Restaurant(
            dto.id,
            dto.name,
            dto.latitude,
            dto.longitude,
            dto.votes,
            dto.cuisines,
            dto.apiMeals
        )
}

class RestaurantsMapper(
    private val restaurantMapper: RestaurantMapper
) {

    fun map(dtos: Array<ApiRestaurantDto>): List<Restaurant> =
        dtos.map { restaurantMapper.map(it) }
}
