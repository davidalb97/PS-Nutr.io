package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal

class SimplifiedMealInputMapper() {
    fun mapToModel(dto: SimplifiedMealInput) =
        Meal(
            dbId = 0,
            submissionId = dto.id,
            name = dto.name,
            isFavorite = if (dto.isFavorite) 1 else 0,
            imageUrl = dto.image.toString()
        )

    fun mapToListModel(dtos: Iterable<SimplifiedMealInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedMealInput>) = dtos.map(::mapToModel)
}