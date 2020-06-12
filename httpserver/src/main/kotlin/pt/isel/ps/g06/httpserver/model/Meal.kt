package pt.isel.ps.g06.httpserver.model

import java.net.URI

data class Meal(
        val identifier: Int,
        val name: String,
        val isFavorite: Boolean?,
        val image: URI?,
        val nutritionalValues: Lazy<NutritionalValues>,
        val ingredients: Sequence<MealIngredient>,
        val cuisines: Sequence<String>,
        val userInfo: Lazy<User?>
)