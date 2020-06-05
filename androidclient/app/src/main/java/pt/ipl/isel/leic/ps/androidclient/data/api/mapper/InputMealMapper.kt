package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputMealDto
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputVotesDto
import pt.ipl.isel.leic.ps.androidclient.data.model.ApiMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class InputMealMapper(private val inputIngredientMapper: InputIngredientMapper) {

    fun mapToModel(dto: InputMealDto) = ApiMeal(
        submissionId = dto.submissionId,
        name = dto.name,
        carbs = dto.carbs,
        amount = dto.amount,
        unit = dto.unit,
        votes = Votes(
            dto.votes.positive,
            dto.votes.negative
        ),
        imageUrl = dto.imageUrl,
        ingredients = inputIngredientMapper.mapToListModel(dto.ingredients)
    )

    fun mapToInput(model: ApiMeal) = InputMealDto(
        submissionId = model.submissionId,
        name = model.name,
        carbs = model.carbs,
        amount = model.amount,
        unit = model.unit,
        votes = InputVotesDto(
            model.votes.positive,
            model.votes.negative
        ),
        imageUrl = model.imageUrl,
        ingredients = inputIngredientMapper.mapToListDto(model.ingredients)
    )

    fun mapToListModel(dtos: Iterable<InputMealDto>) = dtos.map(::mapToModel)
}