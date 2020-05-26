package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.source.dto.CuisineDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine

class CuisineMapper :
    IResponseMapper<CuisineDto, Cuisine> {

    override fun map(dto: CuisineDto): Cuisine =
        Cuisine(dto.name)
}

class CuisinesMapper(
    val mapper: CuisineMapper
) {

    fun map(dtos: Array<CuisineDto>) {
        dtos.map { mapper.map(it) }
    }
}