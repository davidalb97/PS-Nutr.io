package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.modular.*
import java.net.URI

class MealIngredient(
        identifier: Int,
        name: String,
        image: URI?,
        override val nutritionalInfo: NutritionalValues
) : BasePublicSubmission<Int>(
        identifier = identifier,
        image = image,
        name = name
), INameable, IImage, INutritionalSubmission