package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.modular.BasePublicSubmission
import pt.isel.ps.g06.httpserver.model.modular.UserPredicate
import java.net.URI

class MealIngredient(
        identifier: Int,
        name: String,
        isFavorable: UserPredicate,
        isFavorite: UserPredicate,
        image: URI?,
        val nutritionalValues: NutritionalValues
) : BasePublicSubmission<Int>(
        identifier = identifier,
        image = image,
        name = name,
        isFavorable = isFavorable,
        isFavorite = isFavorite
)