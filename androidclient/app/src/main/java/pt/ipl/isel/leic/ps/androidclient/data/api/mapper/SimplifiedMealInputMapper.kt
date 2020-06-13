package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes
import java.net.URI

class SimplifiedMealInputMapper {
    fun mapToModel(dto: SimplifiedMealInput) =
        MealItem(
            dbId = 0,
            submissionId = dto.id,
            imageUrl = dto.image,
            name = dto.name,
            votes = Votes(
                userHasVoted = dto.
            ),
            isFavorite = dto.isFavorite
    )

    fun mapToListModel(dtos: Iterable<SimplifiedMealInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedMealInput>) = dtos.map(::mapToModel)
}