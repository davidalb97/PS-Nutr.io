package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.VotesInputDto
import java.time.OffsetDateTime

class DetailedRestaurantInput(
    id: String,
    name: String,
    latitude: Float,
    longitude: Float,
    votes: VotesInputDto?,
    isFavorite: Boolean?,
    val cuisines: Collection<String>,
    val creationDate: OffsetDateTime?,
    val meals: Collection<SimplifiedRestaurantMealInput>,
    val suggestedMeals: Collection<SimplifiedRestaurantMealInput>
) : SimplifiedRestaurantInput(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    votes = votes,
    isFavorite = isFavorite
)