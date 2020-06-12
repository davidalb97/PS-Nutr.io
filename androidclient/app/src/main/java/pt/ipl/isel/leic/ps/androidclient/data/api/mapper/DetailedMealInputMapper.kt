package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.VotesInputDto
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.MealComposition
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.NutritionalInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo

class DetailedMealInputMapper(private val inputIngredientMapper: InputIngredientMapper) {

    fun mapToModel(dto: DetailedMealInput) = MealInfo(
        submissionId = dto.id,
        name = dto.name,
        carbs = dto.nutritionalInfo!!.carbs,
        amount = dto.nutritionalInfo.amount,
        unit = dto.nutritionalInfo.unit,
        imageUrl = dto.image.toString(),
        ingredients = inputIngredientMapper.mapToListModel(dto.composedBy!!.ingredients)
    )

    /*fun mapToInput(model: MealInfo) =
        DetailedMealInput(
            id = model.submissionId,
            name = model.name,
            image = model.imageUrl,
            isFavorite = model,
            nutritionalInfo = NutritionalInfoInput(model.carbs, model.amount, model.unit),
            composedBy = MealComposition(model.ingredients, model.ingredients)
        )*/

    fun mapToListModel(dtos: Iterable<DetailedMealInput>) = dtos.map(::mapToModel)
}