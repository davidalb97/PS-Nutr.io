package pt.ipl.isel.leic.ps.androidclient.data.model

class MealInfo(
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val imageUrl: String?,
    val ingredients: Iterable<Ingredient>
)