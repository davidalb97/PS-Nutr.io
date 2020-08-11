package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

import android.net.Uri

data class DetailedIngredientInput(
    val id: Int,
    val name: String,
    val imageUri: Uri?,
    val isFavorite: Boolean,
    val nutritionalInfo: NutritionalInfoInput
)