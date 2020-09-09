package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

data class CustomMealOutput(
    val name: String,
    val quantity: Float,
    val unit: String,
    val ingredients: Iterable<IngredientOutput>,
    val cuisines: Iterable<String>,
    val imageUri: String?
)