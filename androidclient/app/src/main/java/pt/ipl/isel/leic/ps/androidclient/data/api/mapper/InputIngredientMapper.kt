package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedIngredientInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.model.Ingredient

class InputIngredientMapper {

    fun mapToModel(dto: DetailedMealInput) = Ingredient(
        submissionId = dto.id,
        name = dto.name,
        carbs = dto.nutritionalInfo!!.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = dto.nutritionalInfo.unit,
        imageUrl = dto.image.toString()
    )

    fun mapToListModel(dtos: Iterable<DetailedMealInput>) = dtos.map(::mapToModel)

    fun mapToDto(model: DetailedMealInput) =
        DetailedIngredientInput(
            submissionId = model.id,
            name = model.name,
            carbs = model.nutritionalInfo!!.carbs,
            amount = model.nutritionalInfo.amount,
            unit = model.nutritionalInfo.unit,
            imageUrl = model.image.toString()
        )

    fun mapToListDto(dtos: Iterable<DetailedMealInput>) = dtos.map(::mapToDto)
}