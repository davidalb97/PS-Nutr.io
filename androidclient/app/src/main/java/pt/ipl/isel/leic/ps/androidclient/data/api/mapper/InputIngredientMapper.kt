package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputIngredientDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Ingredient

class InputIngredientMapper {

    fun mapToModel(dto: InputIngredientDto) = Ingredient(
        submissionId = dto.submissionId,
        name = dto.name,
        carbs = dto.carbs,
        amount = dto.amount,
        unit = dto.unit,
        imageUrl = dto.imageUrl
    )

    fun mapToListModel(dtos: Iterable<InputIngredientDto>) = dtos.map(::mapToModel)

    fun mapToDto(model: Ingredient) = InputIngredientDto(
        submissionId = model.submissionId,
        name = model.name,
        carbs = model.carbs,
        amount = model.amount,
        unit = model.unit,
        imageUrl = model.imageUrl
    )

    fun mapToListDto(dtos: Iterable<Ingredient>) = dtos.map(::mapToDto)
}