package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.VotesInputDto
import java.time.OffsetDateTime

class DetailedRestaurantInput(
    id: String,
    name: String,
    latitude: Float,
    longitude: Float,
    votes: VotesInputDto?,
    isFavorite: Boolean,
    imageUri: Uri?,
    val cuisines: Collection<String>,
    val creationDate: OffsetDateTime?,
    val meals: Collection<SimplifiedMealInput>,
    val suggestedMeals: Collection<SimplifiedMealInput>
) : SimplifiedRestaurantInput(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    votes = votes,
    isFavorite = isFavorite,
    imageUri = imageUri
)