package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedUserInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.VotesInputDto
import java.net.URI
import java.time.OffsetDateTime

class DetailedMealInput(
    id: Int,
    name: String,
    imageUri: Uri?,
    isFavorite: Boolean,
    votes: VotesInputDto?,
    val portions: Collection<Int>,
    val creationDate: OffsetDateTime,
    val composedBy: MealComposition?,
    val nutritionalInfo: NutritionalInfoInput,
    val createdBy: SimplifiedUserInput?,
    val cuisines: Collection<String>
) : SimplifiedMealInput(
    id = id,
    name = name,
    imageUri = imageUri,
    isFavorite = isFavorite,
    votes = votes
)