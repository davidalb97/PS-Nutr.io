package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.ApiCuisineDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine

class CuisineMapper :
    IResponseMapper<ApiCuisineDto, Cuisine> {

    override fun map(dto: ApiCuisineDto): Cuisine =
        Cuisine(dto.name)
}

class CuisinesMapper(
    val mapper: CuisineMapper
) {

    fun map(dtos: Array<ApiCuisineDto>) {
        dtos.map { mapper.map(it) }
    }
}