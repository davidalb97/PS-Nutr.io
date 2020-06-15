package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import android.net.Uri

open class SimplifiedMealInput(
    val mealIdentifier: Int,
    val name: String,
    val imageUri: Uri?,
    val votes: VotesInputDto?,
    val isFavorite: Boolean,
    val isSuggested: Boolean
)