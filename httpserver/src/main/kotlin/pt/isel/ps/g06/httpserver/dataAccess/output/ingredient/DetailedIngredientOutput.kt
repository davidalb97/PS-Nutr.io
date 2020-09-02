package pt.isel.ps.g06.httpserver.dataAccess.output.ingredient

import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.meal.BaseMealOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.INutritionalSubmissionOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.model.MealIngredient
import java.net.URI

class DetailedIngredientOutput(
        identifier: Int,
        name: String,
        image: URI?,
        favorites: FavoritesOutput,
        nutritionalInfo: NutritionalInfoOutput
) : BaseMealOutput(
        identifier = identifier,
        name = name,
        image = image,
        favorites = favorites,
        nutritionalInfo = nutritionalInfo
), INutritionalSubmissionOutput

fun toDetailedIngredientOutput(ingredient: MealIngredient, userId: Int? = null): DetailedIngredientOutput {
    return DetailedIngredientOutput(
            identifier = ingredient.identifier,
            name = ingredient.name,
            image = ingredient.image,
            favorites = FavoritesOutput(
                    isFavorable = true,
                    isFavorite = ingredient.isFavorite(userId)
            ),
            nutritionalInfo = toNutritionalInfoOutput(ingredient.nutritionalValues)
    )
}