package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

import android.net.Uri

data class CustomMealOutput(
    val name: String,
    val quantity: Int,
    val unit: String,
    val ingredients: Iterable<IngredientOutput>,
    val cuisines: Iterable<String>,
    val imageUri: Uri?
)