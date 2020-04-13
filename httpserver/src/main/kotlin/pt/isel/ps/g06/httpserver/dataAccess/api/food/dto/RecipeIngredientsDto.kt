package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto

data class RecipeIngredientsDto(val ingredients: Array<IngredientDto>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeIngredientsDto

        if (!ingredients.contentEquals(other.ingredients)) return false

        return true
    }

    override fun hashCode(): Int {
        return ingredients.contentHashCode()
    }
}

data class IngredientDto(
        val amount: IngredientAmountDto?,
        val image: String?,
        val name: String?
)

data class IngredientAmountDto(
        val metric: IngredientAmountMetricDto?,
        val us: IngredientAmountUsDto?
)

data class IngredientAmountMetricDto(
        val unit: String?,
        val value: Float?
)

data class IngredientAmountUsDto(
        val unit: String?,
        val value: Float?
)