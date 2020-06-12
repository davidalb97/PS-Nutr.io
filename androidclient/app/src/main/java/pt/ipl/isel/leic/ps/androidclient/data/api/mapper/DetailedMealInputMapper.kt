package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.DetailedMealInputDto
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.VotesInputDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DetailedMealInputMapper(private val inputIngredientMapper: InputIngredientMapper) {

    fun mapToModel(dto: DetailedMealInputDto) = Meal(
        submissionId = dto.submissionId,
        name = dto.name,
        carbs = dto.carbs,
        amount = dto.amount,
        unit = dto.unit,
        votes = Votes(
            dto.votes.userHasVoted,
            dto.votes.positive,
            dto.votes.negative
        ),
        imageUrl = dto.imageUrl,
        ingredients = inputIngredientMapper.mapToListModel(dto.ingredients)
    )

    fun mapToInput(model: Meal) =
        DetailedMealInputDto(
            submissionId = model.submissionId,
            name = model.name,
            carbs = model.carbs,
            amount = model.amount,
            unit = model.unit,
            votes = VotesInputDto(
                model.votes.userHasVoted,
                model.votes.positive,
                model.votes.negative
            ),
            imageUrl = model.imageUrl,
            ingredients = inputIngredientMapper.mapToListDto(model.ingredients)
        )

    fun mapToListModel(dtos: Iterable<DetailedMealInputDto>) = dtos.map(::mapToModel)
}