package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.DetailedIngredientInputDto

data class OutputMealDto(
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val imageUrl: String?,
    val ingredients: Iterable<DetailedIngredientInputDto>
)