package pt.ipl.isel.leic.ps.androidclient.data.api.dto

data class InputMealDto(
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val votes: InputVotesDto,
    val imageUrl: String?,
    val ingredients: Iterable<InputIngredientDto>
)