package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

data class DetailedMealInputDto(
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val votes: VotesInputDto,
    val imageUrl: String?,
    val ingredients: Iterable<DetailedIngredientInputDto>
)