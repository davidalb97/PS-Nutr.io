package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import java.time.OffsetDateTime

class DetailedRestaurantInputDto(
    id: String,
    name: String,
    latitude: Float,
    longitude: Float,
    votes: VotesInputDto?,
    isFavorite: Boolean?,
    val cuisines: Collection<String>,
    val creationDate: OffsetDateTime,
    val meals: Collection<SimplifiedRestaurantMealInputDto>,
    val suggestedMeals: Collection<SimplifiedRestaurantMealInputDto>
) : SimplifiedRestaurantInputDto(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    votes = votes,
    isFavorite = isFavorite
)