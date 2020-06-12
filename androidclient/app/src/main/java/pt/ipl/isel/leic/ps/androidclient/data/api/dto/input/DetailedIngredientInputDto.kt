package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

data class DetailedIngredientInputDto(
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val imageUrl: String?
)