package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output


data class MealOutput(
    val name: String,
    val quantity: Int,
    val unit: String,
    val ingredients: Collection<IngredientOutput>,
    val cuisines: Collection<String>
)