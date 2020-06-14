package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import android.net.Uri

open class SimplifiedRestaurantInput(
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: VotesInputDto?,
    val isFavorite: Boolean,
    val imageUri: Uri?
)