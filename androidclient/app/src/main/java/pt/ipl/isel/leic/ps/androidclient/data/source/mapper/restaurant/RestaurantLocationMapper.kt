package pt.ipl.isel.leic.ps.androidclient.data.source.mapper.restaurant

import pt.ipl.isel.leic.ps.androidclient.data.source.dto.restaurant.RestaurantLocationDto
import pt.ipl.isel.leic.ps.androidclient.data.source.mapper.IResponseMapper
import pt.ipl.isel.leic.ps.androidclient.data.source.model.restaurant.RestaurantLocation

class RestaurantLocationMapper : IResponseMapper<RestaurantLocationDto, RestaurantLocation> {

    override fun map(dto: RestaurantLocationDto): RestaurantLocation =
        RestaurantLocation(
            dto.id,
            dto.name,
            dto.latitude,
            dto.longitude
        )
}

class RestaurantLocationsMapper(
    private val mapper: RestaurantLocationMapper
) {

    fun map(dtos: Array<RestaurantLocationDto>): Array<RestaurantLocation> =
        dtos.map(mapper::map).toTypedArray()
}