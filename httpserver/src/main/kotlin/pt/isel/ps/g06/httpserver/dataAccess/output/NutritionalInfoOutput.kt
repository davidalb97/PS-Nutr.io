package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.NutritionalValues

data class NutritionalInfoOutput(
        val carbs: Float,
        val amount: Float,
        val unit: String
)

fun toNutritionalInfoOutput(nutritionalValues: NutritionalValues): NutritionalInfoOutput {
    return NutritionalInfoOutput(
            carbs = nutritionalValues.carbs,
            amount = nutritionalValues.amount,
            unit = nutritionalValues.unit
    )
}