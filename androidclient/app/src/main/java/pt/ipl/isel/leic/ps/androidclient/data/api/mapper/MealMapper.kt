package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.MealDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal

class MealMapper :
    IResponseMapper<MealDto, Meal> {
    override fun map(dto: MealDto): Meal =
        Meal(
            dto.identifier,
            dto.name,
            dto.image_url,
            dto.info // TODO
        )
}

class MealsMapper(
    private val mapper: MealMapper
) {

    fun map(dtos: Array<MealDto>): List<Meal> =
        dtos.map { mapper.map(it) }
}