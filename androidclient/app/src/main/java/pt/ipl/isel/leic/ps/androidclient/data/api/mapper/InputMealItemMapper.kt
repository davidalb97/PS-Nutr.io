package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem

class InputMealItemMapper(
    private val inputVotesMapper: InputVotesMapper
) {
    fun mapToModel(dto: SimplifiedMealInput, isSuggested: Boolean) = MealItem(
            dbId = DbMealItemEntity.DEFAULT_DB_ID,
            dbRestaurantId = DbMealItemEntity.DEFAULT_DB_ID,
            submissionId = dto.id,
            imageUri = dto.imageUri,
            name = dto.name,
            votes = inputVotesMapper.mapToModel(dto.votes),
            isFavorite = dto.isFavorite,
            isSuggested = isSuggested
    )

    fun mapToListModel(dtos: Iterable<SimplifiedMealInput>, isSuggested: Boolean)
            = dtos.map { mapToModel(it, isSuggested)}

    fun mapToListModel(dtos: Array<SimplifiedMealInput>, isSuggested: Boolean)
            = dtos.map { mapToModel(it, isSuggested)}
}