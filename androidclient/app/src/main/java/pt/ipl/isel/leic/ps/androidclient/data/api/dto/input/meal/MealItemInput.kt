package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.favorite.FavoritesInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.vote.VotesInput

open class MealItemInput(
    val identifier: Int,
    val name: String,
    val favorites: FavoritesInput,
    val isSuggested: Boolean?,
    val isVerified: Boolean?,
    val image: Uri?,
    val nutritionalInfo: NutritionalInfoInput,
    val isReportable: Boolean?,
    val votes: VotesInput?
)