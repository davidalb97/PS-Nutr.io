package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine
import java.net.URI
import java.time.OffsetDateTime

data class Meal(
        val identifier: Int,
        val name: String,
        val isFavorite: (Int) -> Boolean,
        val image: URI?,
        val nutritionalValues: NutritionalValues,
        val ingredients: Sequence<MealIngredient>,
        val cuisines: Sequence<Cuisine>,
        val creatorInfo: Lazy<Creator?>,
        val creationDate: Lazy<OffsetDateTime?>
)