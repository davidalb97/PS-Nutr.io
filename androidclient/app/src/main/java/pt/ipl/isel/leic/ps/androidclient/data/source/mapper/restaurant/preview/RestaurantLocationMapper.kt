package pt.ipl.isel.leic.ps.androidclient.data.source.mapper.restaurant.preview

import pt.ipl.isel.leic.ps.androidclient.data.source.dto.restaurant.preview.RestaurantLocationPreviewDto
import pt.ipl.isel.leic.ps.androidclient.data.source.mapper.IResponseMapper
import pt.ipl.isel.leic.ps.androidclient.data.source.model.restaurant.RestaurantLocation

class RestaurantLocationMapper : IResponseMapper<RestaurantLocationPreviewDto, RestaurantLocation> {

    override fun map(dto: RestaurantLocationPreviewDto): RestaurantLocation =
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

    fun map(dtos: Array<RestaurantLocationPreviewDto>): Array<RestaurantLocation> =
        dtos.map(mapper::map).toTypedArray()
}