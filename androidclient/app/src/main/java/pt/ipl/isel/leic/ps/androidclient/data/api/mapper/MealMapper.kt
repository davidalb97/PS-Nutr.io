package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.ApiMealDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal

class MealMapper :
    IResponseMapper<ApiMealDto, Meal> {
    override fun map(dto: ApiMealDto): Meal =
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

    fun map(dtos: Array<ApiMealDto>): List<Meal> =
        dtos.map { mapper.map(it) }
}