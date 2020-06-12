package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

class DetailedIngredientInput(
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val imageUrl: String?
)