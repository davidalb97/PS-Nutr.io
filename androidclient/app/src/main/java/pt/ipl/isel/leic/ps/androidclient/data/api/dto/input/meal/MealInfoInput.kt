package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.cuisine.CuisinesInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.favorite.FavoritesInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.portion.PortionsInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.SimplifiedUserInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.vote.VotesInput

class MealInfoInput(
    identifier: Int,
    name: String,
    image: Uri?,
    votes: VotesInput?,
    favorites: FavoritesInput,
    isSuggested: Boolean?,
    isReportable: Boolean?,
    nutritionalInfo: NutritionalInfoInput,
    isVerified: Boolean?,
    restaurantIdentifier: String?,
    val creationDate: String?,
    val composedBy: MealCompositionInput?,
    val portions: PortionsInput?,
    val createdBy: SimplifiedUserInput?,
    val cuisines: CuisinesInput?
) : MealItemInput(
    identifier = identifier,
    restaurantIdentifier = restaurantIdentifier,
    name = name,
    image = image,
    votes = votes,
    favorites = favorites,
    isSuggested = isSuggested,
    isReportable = isReportable,
    nutritionalInfo = nutritionalInfo,
    isVerified = isVerified
)