package pt.isel.ps.g06.httpserver.dataAccess.output.ingredient

import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.BaseMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.*
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.model.MealIngredient
import java.net.URI

class DetailedIngredientOutput(
        identifier: Int,
        name: String,
        image: URI?,
        override val nutritionalInfo: NutritionalInfoOutput
) : BasePublicSubmissionOutput<Int>(
        identifier = identifier,
        name = name,
        image = image
), INutritionalSubmissionOutput

fun toDetailedIngredientOutput(ingredient: MealIngredient): DetailedIngredientOutput {
    return DetailedIngredientOutput(
            identifier = ingredient.identifier,
            name = ingredient.name,
            image = ingredient.image,
            nutritionalInfo = toNutritionalInfoOutput(ingredient.nutritionalInfo)
    )
}