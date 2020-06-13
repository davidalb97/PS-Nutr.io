package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.VotesInputDto
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.MealComposition
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.NutritionalInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DetailedMealInputMapper(private val inputIngredientMapper: InputIngredientMapper) {

    fun mapToModel(dto: DetailedMealInput) = MealInfo(
        dbId = DbApiMealEntity.DEFAULT_DB_ID,
        submissionId = dto.id,
        name = dto.name,
        carbs = dto.nutritionalInfo!!.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = dto.nutritionalInfo.unit,
        votes = Votes(
            userHasVoted = dto.votes.userHasVoted
        )
        imageUrl = dto.image.toString(),
        ingredients = inputIngredientMapper.mapToListModel(dto.composedBy!!.ingredients)
    )

    fun mapToListModel(dtos: Iterable<DetailedMealInput>) = dtos.map(::mapToModel)
}