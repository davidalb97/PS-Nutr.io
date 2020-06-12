package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInputDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal

class SimplifiedMealInputMapper() {
    fun mapToModel(dto: SimplifiedMealInputDto) =
        Meal()

    fun mapToListModel(dtos: Iterable<SimplifiedMealInputDto>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedMealInputDto>) = dtos.map(::mapToModel)
}