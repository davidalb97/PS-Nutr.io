package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedUserInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.VotesInputDto

class DetailedMealInput(
    id: Int,
    name: String,
    imageUri: Uri?,
    isFavorite: Boolean,
    votes: VotesInputDto?,
    isSuggested: Boolean,
    val portions: Collection<Int>?,
    val creationDate: String?,
    val composedBy: MealComposition?,
    val nutritionalInfo: NutritionalInfoInput,
    val createdBy: SimplifiedUserInput?,
    val cuisines: Collection<String>?
) : SimplifiedMealInput(
    mealIdentifier = id,
    name = name,
    imageUri = imageUri,
    isFavorite = isFavorite,
    votes = votes,
    isSuggested = isSuggested
)