package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.ApiMealDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal

class ApiMealMapper {
    fun mapToModel(apiMealDto: ApiMealDto): Meal =
        Meal(
            apiMealDto.id,
            apiMealDto.name,
            apiMealDto.imageUrl,
            apiMealDto.glucoseAmount,
            apiMealDto.carbsAmount
        )

    fun mapToListModel(dtos: Array<ApiMealDto>): List<Meal> =
        dtos.map { mapToModel(it) }
}