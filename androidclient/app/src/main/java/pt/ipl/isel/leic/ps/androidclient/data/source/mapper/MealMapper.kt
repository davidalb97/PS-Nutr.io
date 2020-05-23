package pt.ipl.isel.leic.ps.androidclient.data.source.mapper

import pt.ipl.isel.leic.ps.androidclient.data.source.dto.MealDto
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal

class MealMapper : IResponseMapper<MealDto, Meal> {
    override fun map(dto: MealDto): Meal =
        Meal(
            dto.identifier,
            dto.name,
            dto.image_url,
            dto.info
        )
}

class MealsMapper(
    private val mapper: MealMapper
) {

    fun map(dtos: Array<MealDto>): List<Meal> =
        dtos.map { mapper.map(it) }
}