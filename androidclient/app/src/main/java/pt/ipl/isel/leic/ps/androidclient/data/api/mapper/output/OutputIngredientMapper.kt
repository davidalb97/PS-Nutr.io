package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.IngredientOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient


class OutputIngredientMapper {

    fun mapToOutputModel(model: MealIngredient) = IngredientOutput(
        identifier = requireNotNull(model.submissionId),
        quantity = model.amount.toInt()
    )

    fun mapToOutputModelCollection(models: Collection<MealIngredient>) =
        models.map(this::mapToOutputModel)
}