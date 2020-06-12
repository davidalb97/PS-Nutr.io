package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal

class SimplifiedMealInputMapper() {
    fun mapToModel(dto: SimplifiedMealInput) =
        Meal(
            submissionId = dto.submission_id,
            name = dto.meal_name,
            votes = dto.votes,
            isFavorite = dto.isFavorite,
            imageUrl = dto.image,
        )

    fun mapToListModel(dtos: Iterable<SimplifiedMealInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedMealInput>) = dtos.map(::mapToModel)
}