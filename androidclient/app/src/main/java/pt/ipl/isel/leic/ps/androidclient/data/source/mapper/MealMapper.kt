package pt.ipl.isel.leic.ps.androidclient.data.source.mapper

import pt.ipl.isel.leic.ps.androidclient.data.source.dto.MealDto
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal

class MealMapper : IResponseMapper<MealDto, Meal> {
    override fun map(dto: MealDto): Meal =
        Meal(
            dto.name,
            dto.apiId,
            dto.apiTypeStr
        )
}

class MealsMapper(
    val mapper: MealMapper
) {

    fun map(dtos: Array<MealDto>) {
        dtos.map { mapper.map(it) }.toTypedArray()
    }
}