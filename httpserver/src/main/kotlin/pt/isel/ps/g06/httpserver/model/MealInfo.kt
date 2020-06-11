package pt.isel.ps.g06.httpserver.model

class MealInfo(
        identifier: Int,
        name: String,
        isFavorite: Boolean,
        image: String?,
        val carbs: Int,
        val amount: Int,
        val unit: String,
        val ingredients: Collection<MealIngredient>,
        val cuisines: Collection<String>
): MealItem(
        identifier = identifier,
        name = name,
        isFavorite = isFavorite,
        image = image
)