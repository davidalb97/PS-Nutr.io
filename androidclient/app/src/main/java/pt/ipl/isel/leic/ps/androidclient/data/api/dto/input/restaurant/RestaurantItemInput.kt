package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurant

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.favorite.FavoritesInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.vote.VotesInput

open class RestaurantItemInput(
    val identifier: String,
    val name: String,
    val image: Uri?,
    val favorites: FavoritesInput,
    val latitude: Float,
    val longitude: Float,
    val votes: VotesInput,
    val isReportable: Boolean
)