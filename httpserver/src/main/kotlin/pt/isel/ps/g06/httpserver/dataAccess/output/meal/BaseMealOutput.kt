package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.BasePublicSubmissionOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.INutritionalSubmissionOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI

open class BaseMealOutput(
        identifier: Int,
        name: String,
        image: URI?,
        favorites: FavoritesOutput,
        override val nutritionalInfo: NutritionalInfoOutput
) : BasePublicSubmissionOutput<Int>(
        identifier = identifier,
        name = name,
        image = image,
        favorites = favorites
), INutritionalSubmissionOutput

fun toBaseMealOutput(meal: Meal, userId: Int? = null): BaseMealOutput {
    return BaseMealOutput(
            identifier = meal.identifier,
            name = meal.name,
            image = meal.image,
            favorites = FavoritesOutput(
                    isFavorite = meal.isFavorite(userId),
                    isFavorable = meal.isFavorable(userId)
            ),
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalInfo)
    )
}