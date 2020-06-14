package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

data class DetailedIngredientInput(
    val id: Int,
    val name: String,
    val isFavorite: Boolean,
    val nutritionalInfo: NutritionalInfoInput
)